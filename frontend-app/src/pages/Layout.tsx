import { memo } from "react";
import {
    Routes,
    Route,
    Link,
    useNavigate,
    useLocation,
    Navigate,
    Outlet,
    useMatch,
    useResolvedPath,
    LinkProps
} from "react-router-dom";

const Layout = memo(() => {
    return (
        <>
            <ul className="navbar-nav bg-gradient-primary sidebar sidebar-dark accordion toggled" id="accordionSidebar">
                <Link to="/" className="sidebar-brand d-flex align-items-center justify-content-center">
                    <div className="sidebar-brand-icon rotate-n-15">
                        <i className="fas fa-laugh-wink"></i>
                    </div>
                </Link>
                <hr className="sidebar-divider my-0"></hr>
                <LeftLink to="/" className="nav-link" >
                    <i className="fas fa-fw fa-tachometer-alt"></i>
                    <span>Dashboard</span>
                </LeftLink>
                <LeftLink to="/input.html" className="nav-link">
                    <i className="fas fa-fw fa-table"></i>
                    <span>Input Data</span>
                </LeftLink>
                <LeftLink to="/charts.html" className="nav-link">
                    <i className="fas fa-fw fa-chart-area"></i>
                    <span>Charts</span>
                </LeftLink>
                <LeftLink to="/settings.html" className="nav-link">
                    <i className="fas fa-fw fa-cogs"></i>
                    <span>Settings</span>
                </LeftLink>
                <hr className="sidebar-divider d-none d-md-block"></hr>
            </ul>
            <div id="content-wrapper" className="d-flex flex-column">
                <div id="content">
                    <nav className="navbar navbar-expand navbar-light bg-white topbar mb-4 static-top shadow">
                        <button id="sidebarToggleTop" className="btn btn-link d-md-none rounded-circle mr-3">
                            <i className="fa fa-bars"></i>
                        </button>
                        <ul className="navbar-nav ml-auto">
                            <li className="nav-item dropdown no-arrow">
                                <a className="nav-link dropdown-toggle" href="#" id="userDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                    <i className="fas fa-fw fa-cog"></i>
                                </a>
                                <div className="dropdown-menu dropdown-menu-right shadow animated--grow-in" aria-labelledby="userDropdown">
                                    <Link to="/profile.html" className="dropdown-item">
                                        <i className="fas fa-user fa-sm fa-fw mr-2 text-gray-400"></i>
                                        <span>Profile</span>
                                    </Link>
                                    <Link to="/settings.html" className="dropdown-item">
                                        <i className="fas fa-cogs fa-sm fa-fw mr-2 text-gray-400"></i>
                                        <span>Settings</span>
                                    </Link>
                                    <Link to="/activitylog.html" className="dropdown-item">
                                        <i className="fas fa-list fa-sm fa-fw mr-2 text-gray-400"></i>
                                        <span>Activity Log</span>
                                    </Link>
                                    <div className="dropdown-divider"></div>
                                    <a className="dropdown-item" href="#" data-toggle="modal" data-target="#logoutModal">
                                        <i className="fas fa-sign-out-alt fa-sm fa-fw mr-2 text-gray-400"></i>
                                        Logout
                                    </a>
                                </div>
                            </li>
                        </ul>
                    </nav>
                    <Outlet />
                </div>
                <footer className="sticky-footer bg-white">
                    <div className="container my-auto">
                        <div className="copyright text-center my-auto">
                            <span>Copyright &copy; Your Website 2021</span>
                        </div>
                    </div>
                </footer>
            </div>
        </>
    );
});

const LeftLink = ({ children, to, ...props }: LinkProps) => {
    const resolved = useResolvedPath(to);
    const match = useMatch({ path: resolved.pathname, end: true });
    return (
        <li className={match != null ? "nav-item active" : "nav-item"}>
            <Link to={to} className="nav-link" {...props}>
                {children}
            </Link>
        </li>
    );
};
export default Layout;