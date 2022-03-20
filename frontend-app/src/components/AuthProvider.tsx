import { createContext } from "react";

export const AuthContext = createContext({});


interface ChildProps {
    children: React.ReactNode
}

export const AuthProvider = ({ children }: ChildProps) => {
    const obj = {};
    return (
        <AuthContext.Provider value={{
            func: () => {
                console.log("hello world");
            }
        }}>
            {children}
        </AuthContext.Provider>
    );
}

export const LogoutFn = () => {
    fetch("/auth/logout", {
        method: "POST",
        cache: 'no-cache'
    });
}

export const GetAccessTokenFn = () => {
    /*fetch("/auth/getAccessToken", {
        method: "POST",
        cache: 'no-cache'
    }).then(res => res.text()).then(text => { console.log(text); accessToken = text; });*/
}

export const Print = () => {
    //console.log(accessToken);
}