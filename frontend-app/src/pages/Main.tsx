import { memo } from "react";

interface ChildProps {
    onClick: () => void;
    children: React.ReactNode
}

const Main = memo(({ onClick, children }: ChildProps) => {
    return (
        <>
            <p><button onClick={onClick}>logout</button><br /></p>
        </>
    )
});

export default Main;