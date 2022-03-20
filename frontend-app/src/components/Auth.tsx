
let access: string = "";
export const ajax = (url: string, data: object | null, success: ((req: XMLHttpRequest) => void) | undefined | null = null, error: ((req: XMLHttpRequest) => void) | undefined | null = null, sync = true) => {
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

export const CreateTokenCode = (id: string, pw: string) => {
    let ret = false;
    ajax("/auth/login.auth", {
        id,
        pw
    }, (req) => {
        let header = req.getResponseHeader("X-AUTH-TOKEN-ACCESS");
        if (header != null) {
            access = header;
            ret = true;
        }
    }, null, false);
    return ret;
}

export const CheckAccessCode = (): Boolean => {
    let ret = false;
    ajax("/auth/check.auth", null, (req) => {
        ret = true;
    }, (req) => {
        ret = false;
    }, false);
    return ret;
}

export const RefreshAccessCode = () => {
    ajax("/auth/refesh.auth", null, (req) => {
        let header = req.getResponseHeader("X-AUTH-TOKEN-ACCESS");
        if (header != null) {
            access = header;
        }
    }, null, false);
}

export const RemoveTokenCode = () => {
    ajax("/auth/logout.auth", null, (req) => {
        access = "";
    }, (req) => {
        access = "";
    }, false);
}