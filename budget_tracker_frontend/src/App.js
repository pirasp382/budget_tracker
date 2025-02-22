import {BrowserRouter, Routes} from "react-router"
import Login from "./pages/login/Login"
import {useEffect, useState} from "react"
import {Route} from "react-router-dom"
import Register from "./pages/register/Register"
import {PrivateRoute} from "./components/private-route/PrivateRoute"
import Dashboard from "./pages/dashboard/Dashboard"
import Transactions from "./pages/transactions/Transactions"
import Accounts from "./pages/accounts/Accounts"
import Caregories from "./pages/categories/Categories"
import Settings from "./pages/settings/Settings"


const App = () => {

    const [isLoggedIn, setIsLoggedIn] = useState(null)
    const [loading, setLoading] = useState(true)


    useEffect(() => {
        const token = localStorage.getItem("jwt")
        if (token) {
            setIsLoggedIn(true)
        } else {
            setIsLoggedIn(false)
        }
        setLoading(false)
    }, [])

    return <div>
        <BrowserRouter>
            <Routes>
                <Route path={"/"} element={
                    <PrivateRoute isLoggedIn={isLoggedIn}>
                        <Dashboard/>
                    </PrivateRoute>
                }/>
                <Route path={"/transactions"} element={
                    <PrivateRoute isLoggedIn={isLoggedIn}>
                        <Transactions/>
                    </PrivateRoute>
                }/>
                <Route path={"/accounts"} element={
                    <PrivateRoute isLoggedIn={isLoggedIn}>
                        <Accounts/>
                    </PrivateRoute>
                }/>
                <Route path={"/categories"} element={
                    <PrivateRoute isLoggedIn={isLoggedIn}>
                        <Caregories/>
                    </PrivateRoute>
                }/>
                <Route path={"/settings"} element={
                    <PrivateRoute isLoggedIn={isLoggedIn}>
                        <Settings/>
                    </PrivateRoute>
                }/>
                <Route path={"/login"} element={
                    <Login setIsLoggedIn={setIsLoggedIn}/>}/>
                <Route path={"/register"} element={
                    <Register setIsLoggedIn={setIsLoggedIn}/>}/>
            </Routes>
        </BrowserRouter>
    </div>
}

export default App
