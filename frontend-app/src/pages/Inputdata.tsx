import { title } from "process";
import { memo } from "react";
import { SymbolDisplayPartKind } from "typescript";
import styleSheets from "./Inputdata.module.scss";
const Inputdata = memo(() => {
    return (
        <div className="container-fluid">
            <div className={styleSheets.card}>
                <div className={styleSheets.title} >
                    Receipt
                </div>
                <div>
                    <button>Payment</button>
                    <button>Income</button>
                </div>
                <div>
                    <input type="text"></input>
                </div>
                <div>
                    <input type="number"></input>
                </div>
                <div>
                    <div>
                        <button>7</button>
                        <button>8</button>
                        <button>9</button>
                        <button>/</button>
                    </div>
                    <div>
                        <button>4</button>
                        <button>5</button>
                        <button>6</button>
                        <button>*</button>
                    </div>
                    <div>
                        <button>1</button>
                        <button>2</button>
                        <button>3</button>
                        <button>-</button>
                    </div>
                    <div>
                        <button>000</button>
                        <button>0</button>
                        <button>.</button>
                        <button>+</button>
                    </div>
                    <div>
                        <button>Apply</button>
                        <button>←</button>
                    </div>
                </div>
            </div>
        </div>
    );
});
export default Inputdata;