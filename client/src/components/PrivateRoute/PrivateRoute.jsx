import {useContext} from 'react';
import {Navigate, useLocation} from 'react-router-dom';
import {UserContext} from '../../contexts/UserContext';
import PropTypes from 'prop-types';
import {CompetitorProvider} from "../../contexts/CompetitorContext.jsx";

const PrivateRoute = ({children}) => {
    const {user} = useContext(UserContext);
    const location = useLocation(); // used for redirecting to previous location

    // an invalid user can be exploited to visit private routes, eventually it
    // would make sense to add a sever call to verify user's token is still active
    return user ? <CompetitorProvider> {children} </CompetitorProvider> :
        <Navigate to="/login" state={{from: location}}/>;
}

PrivateRoute.propTypes = {
    children: PropTypes.node,
};

export default PrivateRoute;