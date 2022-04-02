import { memo } from "react";
import {
    Routes,
    Route,
    Link,
    useNavigate,
    useLocation,
    Navigate,
    Outlet,
} from "react-router-dom";
import ActivityLog from "./ActivityLog";
import Charts from "./Charts";
import Dashboard from "./DashBoard";
import Inputdata from "./Inputdata";
import Layout from "./Layout";
import Profile from "./Profile";
import Settings from "./Settings";

interface ChildProps {
    onSignOut: () => void;
    children: React.ReactNode
}

const MainPage = memo(({ onSignOut, children }: ChildProps) => {
    return (
        <>
            <div id="wrapper">
                <Routes>
                    <Route element={<Layout></Layout>}>
                        <Route path="/input.html" element={<Inputdata></Inputdata>} />
                        <Route path="/charts.html" element={<Charts></Charts>} />
                        <Route path="/settings.html" element={<Settings></Settings>} />
                        <Route path="/profile.html" element={<Profile></Profile>} />
                        <Route path="/activitylog.html" element={<ActivityLog></ActivityLog>} />
                        <Route path="*" element={<Dashboard></Dashboard>} />
                    </Route>
                </Routes>
            </div>
            <a className="scroll-to-top rounded" href="#page-top">
                <i className="fas fa-angle-up"></i>
            </a>
            <div className="modal fade" id="logoutModal" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                <div className="modal-dialog" role="document">
                    <div className="modal-content">
                        <div className="modal-header">
                            <h5 className="modal-title" id="exampleModalLabel">Ready to Leave?</h5>
                            <button className="close" type="button" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">×</span>
                            </button>
                        </div>
                        <div className="modal-body">Select "Logout" below if you are ready to end your current session.</div>
                        <div className="modal-footer">
                            <button className="btn btn-secondary" type="button" data-dismiss="modal">Cancel</button>
                            <button className="btn btn-primary" onClick={onSignOut}>Logout</button>
                        </div>
                    </div>
                </div>
            </div>
        </>
    )
});

export default MainPage;