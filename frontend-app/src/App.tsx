import logo from './logo.svg';
import './App.css';
import React from 'react';
import { useContext, useEffect, useState } from 'react';
import MainPage from './pages/MainPage'
import LoginPage from './pages/LoginPage';
import Loader from './components/Loader'
/*import { createTokenCode, getAccessCode } from './auth/Auth';*/
import { AuthProvider, RequireAuth, useAuth } from './auth/AuthProvider';
import {
  Routes,
  Route,
  Link,
  useNavigate,
  useLocation,
  Navigate,
  Outlet,
} from "react-router-dom";

function App() {
  let auth = useAuth();
  function onKeyPress(e: React.KeyboardEvent) {
    if (e.key === "Enter") {
      auth.signin();
    }
  }
  return (
    <Routes>
      <Route>
        <Route path="/login.html" element={<LoginPage onClick={() => { auth.signin(); }} onKeyPress={onKeyPress}></LoginPage>} />
        <Route path="*" element={
          <RequireAuth>
            <MainPage onClick={() => { auth.signout(); }}>logout</MainPage>
          </RequireAuth>
        }
        />
      </Route>
    </Routes>
  );
}

export default App;
