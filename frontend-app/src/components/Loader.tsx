import { BlobOptions } from "buffer";

let Loader = (function ($) {
    $.init();
    return $;
})((function () {
    let preventReload = true;
    let isLoading = false;
    let dom1 = document.createElement('div');
    dom1.className = "loader";
    let dom2 = document.createElement('section');
    dom2.className = "loader-layout";
    return {
        init: function () {
            window.onbeforeunload = function (e) {
                if (preventReload && isLoading) {
                    return true;
                }
            }
            document.onkeydown = function (e: KeyboardEvent) {
                if (preventReload && isLoading) {
                    if ((e.ctrlKey == true && (e.keyCode === 78 || e.keyCode === 82)) || (e.keyCode === 116)) {
                        //e.keyCode = 0;
                        e.cancelBubble = true;
                        e.returnValue = false;
                    }
                }
            }
            document.body.appendChild(dom1);
            document.body.appendChild(dom2);
        },
        setStyle: function (style: string) {
            let elements = document.getElementsByClassName("loader");
            for (let i = 0; i < elements.length; i++) {
                elements[i].classList.add(style);
            }
        },
        setReload: function (is: boolean) {
            if (is === false) {
                preventReload = false;
            } else {
                preventReload = true;
            }
        },
        on: function (cb: (() => void) | undefined | null = null) {
            isLoading = true;
            let elements = document.getElementsByClassName("loader");
            for (let i = 0; i < elements.length; i++) {
                elements[i].classList.add("on");
            }
            elements = document.getElementsByClassName("loader-layout");
            for (let i = 0; i < elements.length; i++) {
                elements[i].classList.add("on");
            }
            let layout = document.getElementsByClassName("loader-layout");
            if (cb !== null && cb !== undefined) {
                cb.call(this);
            }
        },
        off: function (cb: (() => void) | undefined | null = null) {
            isLoading = false;
            let elements = document.getElementsByClassName("loader");
            for (let i = 0; i < elements.length; i++) {
                elements[i].classList.remove("on");
            }
            elements = document.getElementsByClassName("loader-layout");
            for (let i = 0; i < elements.length; i++) {
                elements[i].classList.remove("on");
            }
            if (cb !== null && cb !== undefined) {
                cb.call(this);
            }
        }
    }
})());
export default Loader;