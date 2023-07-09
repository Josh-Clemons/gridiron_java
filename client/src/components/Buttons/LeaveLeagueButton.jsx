import React, { useContext } from 'react';
import axios from 'axios';
import Box from '@mui/material/Box';

import ExitToAppIcon from '@mui/icons-material/ExitToApp';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import Stack from '@mui/material/Stack';
import Modal from '@mui/material/Modal';
import { useNavigate } from 'react-router-dom';
import { UserContext } from '../../contexts/UserContext';
import PropTypes from 'prop-types';


const style = {
    position: 'absolute',
    top: '20%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: '95%',
    maxWidth: 400,
    bgcolor: 'background.paper',
    border: '2px solid #000',
    boxShadow: 24,
    p: 4,
};

const LeaveLeagueButton = ({leagueDetails}) => {
    const navigate = useNavigate();
    const {user} = useContext(UserContext);

    // open, handleOpen/Close are all used to control the modal
    const [open, setOpen] = React.useState(false);
    const handleOpen = () => setOpen(true);
    const handleClose = () => setOpen(false);

    // sends league ID as payload to know what to delete, server confirms user making request is league owner
    const handleSubmit = async (event) => {
        event.preventDefault();
        await axios.delete(`http://localhost:8080/api/league/leave?leagueId=${leagueDetails.id}`,
      {
        headers: {
          'Authorization': `Bearer ${user.accessToken}`
        }
      })
      .then(() => {
        navigate('/dashboard');
      }).catch(() => {
        window.alert('error leaving league');
      })
    };

    return (
      <Box>
          <Button variant="outlined" onClick={handleOpen} color={'error'} size='small' sx={{ width: 125, m: 1, borderWidth: 2, '&:hover': { borderWidth: '2px' } }}>Leave<ExitToAppIcon sx={{ ml: 2 }} /></Button>
          <Modal
              open={open}
              onClose={handleClose}
          >
              <Box sx={style} component='form' onSubmit={handleSubmit}>
                  <Typography id="modal-modal-title" variant="h6">
                      Are you sure you want to leave?
                  </Typography>
                  <Stack spacing={1} p={2} direction='row-reverse'>
                      <Button variant='outlined' color={'error'} onClick={handleSubmit} sx={{ width: 80 }}>Yes</Button>
                      <Button variant='outlined' color={'warning'} onClick={handleClose} sx={{ width: 80 }}>Cancel</Button>
                  </Stack>
              </Box>
          </Modal>
      </Box>
  );
}

LeaveLeagueButton.propTypes = {
  leagueDetails: PropTypes.object
}

export default LeaveLeagueButton;