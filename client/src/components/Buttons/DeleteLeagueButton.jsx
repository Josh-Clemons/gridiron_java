// Libraries
import axios from 'axios';
import PropTypes from 'prop-types';
import {useContext, useState} from 'react';
import {useNavigate} from 'react-router-dom';

// UI Components
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Modal from '@mui/material/Modal';
import Stack from '@mui/material/Stack';
import Typography from '@mui/material/Typography';

// Icons
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import DeleteOutlineOutlinedIcon from '@mui/icons-material/DeleteOutlineOutlined';

// Contexts
import {UserContext} from '../../contexts/UserContext';

// Utils
import {errorAlert, infoAlert} from '../../utils/ToastAlerts';

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

const DeleteLeagueButton = ({leagueDetails}) => {
    const {user} = useContext(UserContext);
    const navigate = useNavigate();

    const [open, setOpen] = useState(false);

    const handleOpen = () => setOpen(true);
    const handleClose = () => setOpen(false);

    const handleSubmit = async (event) => {
        event.preventDefault();
        await axios.delete(`${window.BACKEND_URL}/api/league/delete?leagueId=${leagueDetails.id}`,
            {
                headers: {
                    'Authorization': `Bearer ${user.accessToken}`
                }
            })
            .then(() => {
                infoAlert('League deleted');
                navigate('/dashboard');
            }).catch(() => {
                errorAlert('Error deleting league');
            })
    };

    return (
        <Box>
            <Button
                variant="outlined"
                onClick={handleOpen}
                color='error'
                size='small'
                sx={{width: 125, m: 1, borderWidth: 2, '&:hover': {borderWidth: '2px'}}}
            >
                DELETE
                <DeleteOutlineOutlinedIcon sx={{ml: 2}}/>
            </Button>
            <Modal
                open={open}
                onClose={handleClose}
            >
                <Box sx={style} component='form' onSubmit={handleSubmit}>
                    <Typography id="modal-modal-title" variant="h6">
                        Are you sure you want to delete?
                    </Typography>
                    <Stack spacing={1} p={2} direction='row-reverse'>
                        <Button
                            variant='outlined'
                            color='error'
                            onClick={handleSubmit}
                            sx={{minWidth: 80, borderWidth: 2}}
                        >
                            Yes
                            <DeleteOutlineOutlinedIcon sx={{ml: 1}}/>
                        </Button>
                        <Button
                            variant='outlined'
                            color='warning'
                            onClick={handleClose}
                            sx={{minWidth: 80, borderWidth: 2}}
                        >
                            Cancel
                            <ArrowBackIcon sx={{ml: 1}}/>
                        </Button>
                    </Stack>
                </Box>
            </Modal>
        </Box>
    );
}

DeleteLeagueButton.propTypes = {
    leagueDetails: PropTypes.object.isRequired,
};

export default DeleteLeagueButton;