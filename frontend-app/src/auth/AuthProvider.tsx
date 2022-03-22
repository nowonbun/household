import * as React from "react";
import { useEffect } from "react";
import { createContext } from "react";
import { Routes, Route, Link, useNavigate, useLocation, Navigate, Outlet } from "react-router-dom";
import Loader from "../components/Loader";


export const RequireAuth = ({ children }: { children: JSX.Element }) => {
    let location = useLocation();
    let auth = useAuth();
    if (auth.getAccessCode() === "") {
        if (auth.checkAccessCode()) {
            auth.refreshAccessCode();
            return children;
        }
        return <Navigate to="/login.html" state={{ from: location }} replace />;
    }
    return children;
}

interface AuthContextType {
    errorMessage: string,
    signin: () => void;
    signout: () => void;
    ajax: (url: string, data: object | null, success: ((req: XMLHttpRequest) => void) | undefined | null, error: ((req: XMLHttpRequest) => void) | undefined | null, sync: boolean) => void;
    createTokenCode: (id: string, pw: string) => boolean;
    checkAccessCode: () => boolean;
    refreshAccessCode: () => void;
    removeTokenCode: () => void;
    getAccessCode: () => string;
}

const AuthContext = React.createContext<AuthContextType>(null!);

export const useAuth = () => {
    return React.useContext(AuthContext);
}

export const AuthProvider = ({ children }: { children: React.ReactNode }) => {
    Loader.setReload(true);
    const location: any = useLocation();
    const navigate = useNavigate();
    const [errorMessage, setErrorMessage] = React.useState("");
    const [access, setAccess] = React.useState("");
    useEffect(() => {
        setInterval(() => {
            if (getAccessCode() !== "") {
                refreshAccessCode();
            }
        }, (1000 * 60 * 9) + (1000 * 30));
    }, []);

    let signin = () => {
        Loader.on();
        let id = (document.getElementById("uid") as HTMLInputElement).value;
        let pw = (document.getElementById("upw") as HTMLInputElement).value;
        if (id.trim() == "" || pw.trim() == "") {
            setErrorMessage("Emailやパスワードを入力してください。");
            Loader.off();
            return;
        }
        if (createTokenCode(id, pw)) {
            let from = location.state?.from?.pathname || "/";
            navigate(from, { replace: true });
            setErrorMessage("");
        } else {
            setErrorMessage("Emailやパスワードが間違いです。");
        }
        Loader.off();
    };

    let signout = () => {
        Loader.on();
        removeTokenCode();
        Loader.off();
    };

    let ajax = (url: string, data: object | null, success: ((req: XMLHttpRequest) => void) | undefined | null = null, error: ((req: XMLHttpRequest) => void) | undefined | null = null, sync = true): void => {
        let xhr = new XMLHttpRequest();
        let json = "";
        xhr.onreadystatechange = (e) => {
            let req = e.target as XMLHttpRequest;
            if (req == null) {
                return;
            }
            if (req.readyState === XMLHttpRequest.DONE) {
                if (req.status === 200) {
                    if (success !== null && success !== undefined) {
                        success.call(this, req);
                    }
                } else {
                    if (error !== null && error !== undefined) {
                        error.call(this, req);
                    }
                }
            }
        }
        if (data != null) {
            json = JSON.stringify(data);
        }
        xhr.open("POST", url, sync);
        xhr.setRequestHeader('Content-Type', 'application/json');
        xhr.setRequestHeader('Cache-Control', 'no-cache');
        xhr.setRequestHeader('X-AUTH-TOKEN-ACCESS', access);
        xhr.send(json);
    };

    let createTokenCode = (id: string, pw: string): boolean => {
        let ret = false;
        ajax("/auth/login.auth", {
            id,
            pw
        }, (req) => {
            let header = req.getResponseHeader("X-AUTH-TOKEN-ACCESS");
            if (header != null) {
                setTimeout(() => {
                    if (header != null) {
                        setAccess(header);
                    }
                });
                ret = true;
            }
        }, null, false);
        return ret;
    }

    let checkAccessCode = (): boolean => {
        let ret = false;
        ajax("/auth/check.auth", null, (req) => {
            if (Number(req.responseText) > 0) {
                ret = true;
            } else {
                ret = false;
            }
        }, (req) => {
            ret = false;
        }, false);
        return ret;
    }

    let refreshAccessCode = (): void => {
        ajax("/auth/refesh.auth", null, (req) => {
            let header = req.getResponseHeader("X-AUTH-TOKEN-ACCESS");
            setTimeout(() => {
                if (header != null) {
                    setAccess(header);
                }
            });
        }, null, false);
    }

    let removeTokenCode = (): void => {
        ajax("/auth/logout.auth", null, (req) => {
            setTimeout(() => {
                setAccess("")
            });
        }, (req) => {
            setTimeout(() => {
                setAccess("")
            });
        }, false);
    }

    let getAccessCode = (): string => {
        return access;
    }

    return <AuthContext.Provider value={{ errorMessage, signin, signout, ajax, createTokenCode, checkAccessCode, refreshAccessCode, removeTokenCode, getAccessCode }}>{children}</AuthContext.Provider>;
}