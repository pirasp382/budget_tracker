import {Navigate} from "react-router"
import PropTypes from "prop-types"

export const PrivateRoute = ({children, isLoggedIn}) => {
    if (isLoggedIn === null) {
        return <div>Logging in...</div>
    }
    return isLoggedIn ? children : <Navigate to={"/"}/>
}

PrivateRoute.propTypes = {
    children: PropTypes.node,
    isLoggedIn: PropTypes.bool,
}