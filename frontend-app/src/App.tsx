import logo from './logo.svg';
import './App.css';
import React from 'react';
import { useContext, useEffect, useState } from 'react';
import Main from './pages/Main'
import Login from './pages/Login';
import { AuthContext } from './components/AuthProvider';
import { CreateTokenCode, CheckAccessCode, RefreshAccessCode, RemoveTokenCode } from './components/Auth';
import Loader from './components/Loader'

let loginstate = false;

function App() {
  Loader.setReload(true);
  /*const authvalue = useContext(AuthContext);*/
  const [isLogin, setIsLogin] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");
  /*const [loading, setLoading] = useState(false);*/
  useEffect(() => {
    if (CheckAccessCode()) {
      setIsLogin(true);
    }
    setInterval(() => {
      if (loginstate) {
        RefreshAccessCode();
      }
    }, (1000 * 60 * 9) + (1000 * 30));
  }, []);

  useEffect(() => {
    loginstate = isLogin;
  }, [isLogin]);

  function login() {
    Loader.on();
    let id = (document.getElementById("uid") as HTMLInputElement).value;
    let pw = (document.getElementById("upw") as HTMLInputElement).value;
    if (id.trim() == "" || pw.trim() == "") {
      setErrorMessage("Emailやパスワードを入力してください。");
      Loader.off();
      return;
    }
    if(CreateTokenCode(id, pw)){
      setErrorMessage("");
      setIsLogin(true);
    } else {
      setErrorMessage("Emailやパスワードが間違いです。");
    }
    Loader.off();
  }

  function onKeyPress(e: React.KeyboardEvent) {
    if (e.key === "Enter") {
      login();
    }
  }

  function logout() {
    RemoveTokenCode();
    setIsLogin(false);
  }

  if (isLogin) {
    return (
      <>
        <Main onClick={logout}>logout</Main>
      </>
    );
  } else {
    return (
      <>
        <Login onClick={login} onKeyPress={onKeyPress} errorMessage={errorMessage}></Login>
      </>
    );
  }
}

export default App;
