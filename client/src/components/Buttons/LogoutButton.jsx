// Libraries
import {Button} from '@mui/material';
import PropTypes from 'prop-types';
import SearchIcon from '@mui/icons-material/Search';
import { useContext } from 'react';

// Contexts
import { UserContext } from '../../contexts/UserContext';

const LogoutButton = ({width}) => {

    const {signOut} = useContext(UserContext);

    return (
        <Button variant="contained" color="error" onClick={() => signOut()}
                sx={{width: width, m: 1, borderWidth: 2, '&:hover': {borderWidth: '2px'}}}>Logout<SearchIcon
            sx={{ml: 2}}/></Button>
    )
}
LogoutButton.propTypes = {
    width: PropTypes.number
}
export default LogoutButton;