import { Button } from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import { useNavigate } from 'react-router-dom';
import PropTypes from 'prop-types';
import { useContext } from 'react';
import { UserContext } from '../../contexts/UserContext';
import axios from 'axios';

const JoinLeagueButton = ({ width, size, leagueDetails }) => {
  const navigate = useNavigate();
  const { user } = useContext(UserContext);

  // TODO update so invite codes work
  const joinLeague = async () => {
    await axios.post('http://localhost:8080/api/league/join',
      { leagueId: leagueDetails.id },
      {
        headers: {
          'Authorization': `Bearer ${user.accessToken}`
        }
      })
      .then(() => {
        navigate(0);
      }).catch(() => {
        window.alert('error joining league');
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
  size:PropTypes.string,
  leagueDetails: PropTypes.object
}
export default JoinLeagueButton;