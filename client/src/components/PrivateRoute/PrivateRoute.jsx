// Libraries
import {Navigate, useLocation} from 'react-router-dom';
import PropTypes from 'prop-types';
import {useContext} from 'react';

// Contexts
import {CompetitorProvider} from "../../contexts/CompetitorContext.jsx";
import {UserContext} from '../../contexts/UserContext';

const PrivateRoute = ({children}) => {
    // Hooks
    const {user} = useContext(UserContext);
    const location = useLocation();

    return user
        ? <CompetitorProvider>{children}</CompetitorProvider>
        : <Navigate to="/login" state={{from: location}}/>;
};

PrivateRoute.propTypes = {
    children: PropTypes.node,
};

export default PrivateRoute;