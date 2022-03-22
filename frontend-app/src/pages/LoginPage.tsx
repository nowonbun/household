import { useContext, useEffect, useState, memo } from 'react';
import { useAuth } from '../auth/AuthProvider';

interface ChildProps {
    onKeyPress: (e: React.KeyboardEvent) => void;
    onClick: () => void;
}

const LoginPage = memo(({ onKeyPress, onClick }: ChildProps) => {
    let auth = useAuth();
    return (
        <div className="container">
            <div className="row justify-content-center">
                <div className="col-xl-5 col-lg-6 col-md-9">
                    <div className="card o-hidden border-0 shadow-lg my-5">
                        <div className="card-body p-0">
                            <div className="row">
                                <div className="col-lg-12">
                                    <div className="p-5">
                                        <div className="text-center">
                                            <h1 className="h4 text-gray-900 mb-4">Account book</h1>
                                        </div>
                                        <div className='text-danger p-2'>
                                            {auth.errorMessage}
                                        </div>
                                        <form className="user">
                                            <div className="form-group">
                                                <input type="email" className="form-control form-control-user" id="uid" aria-describedby="emailHelp" placeholder="Enter Email Address..." onKeyPress={onKeyPress} />
                                            </div>
                                            <div className="form-group">
                                                <input type="password" className="form-control form-control-user" id="upw" placeholder="Password" onKeyPress={onKeyPress} />
                                            </div>
                                            <a className="btn btn-primary btn-user btn-block" onClick={onClick}>
                                                Login
                                            </a>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
});

export default LoginPage;
