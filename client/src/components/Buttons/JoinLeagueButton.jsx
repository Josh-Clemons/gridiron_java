// Libraries
import axios from 'axios';
import PropTypes from 'prop-types';
import {useContext} from 'react';
import {useQueryClient} from 'react-query';
import AddIcon from '@mui/icons-material/Add';
import {Button} from '@mui/material';

// Contexts
import {UserContext} from '../../contexts/UserContext';

// Utilities
import {errorAlert, successAlert} from '../../utils/ToastAlerts';

const JoinLeagueButton = ({width, size, leagueDetails}) => {
    const {user} = useContext(UserContext);
    const queryClient = useQueryClient();

    const joinLeague = async () => {
        await axios.post(
            `${window.BACKEND_URL}/api/league/join`,
            {
                leagueId: leagueDetails.id,
                inviteCode: leagueDetails.inviteCode
            },
            {
                headers: {
                    'Authorization': `Bearer ${user.accessToken}`
                }
            }
        ).then(() => {
            successAlert('League joined!');
            queryClient.invalidateQueries('leaguePicks');
            queryClient.invalidateQueries('leagueDetails');
        }).catch(() => {
            errorAlert("Error joining league...");
        })
    }

    return (
        <Button
            variant="outlined"
            size={size}
            color="success"
            onClick={() => joinLeague()}
            sx={{width: width, m: 1, borderWidth: 2, '&:hover': {borderWidth: '2px'}}}
        >
            Join
            <AddIcon sx={{ml: 2}}/>
        </Button>
    )
}

JoinLeagueButton.propTypes = {
    width: PropTypes.number,
    size: PropTypes.string,
    leagueDetails: PropTypes.object
}

export default JoinLeagueButton;
