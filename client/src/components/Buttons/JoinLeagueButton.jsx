import { Button } from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import PropTypes from 'prop-types';
import { useContext } from 'react';
import { UserContext } from '../../contexts/UserContext';
import axios from 'axios';
import { useQueryClient } from 'react-query';
import { errorAlert, successAlert } from '../../utils/ToastAlerts';

const JoinLeagueButton = ({ width, size, leagueDetails }) => {
  const queryClient = useQueryClient();
  const { user } = useContext(UserContext);

  const joinLeague = async () => {
    await axios.post('http://localhost:8080/api/league/join',
      {
        leagueId: leagueDetails.id,
        inviteCode: leagueDetails.inviteCode
      },
      {
        headers: {
          'Authorization': `Bearer ${user.accessToken}`
        }
      })
      .then(() => {
        successAlert('League joined!')
        queryClient.invalidateQueries('leaguePicks');
        queryClient.invalidateQueries('leagueDetails');
      }).catch(() => {
        errorAlert("Error joining league...");
      })
  }

  return (
    <Button variant="outlined" size={size} color="success" onClick={() => joinLeague()}
      sx={{ width: width, m: 1, borderWidth: 2, '&:hover': { borderWidth: '2px' } }}>Join<AddIcon
        sx={{ ml: 2 }} /></Button>
  )
}
JoinLeagueButton.propTypes = {
  width: PropTypes.number,
  size: PropTypes.string,
  leagueDetails: PropTypes.object
}
export default JoinLeagueButton;