// Libraries
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Modal from '@mui/material/Modal';
import SearchIcon from '@mui/icons-material/Search';
import Stack from '@mui/material/Stack';
import TextField from '@mui/material/TextField';
import Typography from '@mui/material/Typography';

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

const JoinLeagueByCodeButton = () => {
    const navigate = useNavigate();

    const [inviteCode, setInviteCode] = useState('');
    const [open, setOpen] = useState(false);

    const handleOpen = () => setOpen(true);
    const handleClose = () => setOpen(false);

    const handleSubmit = (event) => {
        event.preventDefault();
        handleClose();
        navigate(`/league-detail/${inviteCode}`);
    };

    return (
        <Box>
            <Button variant="outlined" onClick={handleOpen} color={'success'} sx={{ width: 250, mb: 2, borderWidth: 2, '&:hover': { borderWidth: '2px' } }}>Find by Invite Code<SearchIcon sx={{ ml: 2 }} /></Button>
            <Modal
                open={open}
                onClose={handleClose}
            >
                <Box sx={style} component='form' onSubmit={handleSubmit}>
                    <Typography id="inviteCodeTitle" variant="h6" component="h2">
                        Enter Invite Code
                    </Typography>
                    <TextField
                        required
                        id="inviteCodeInput"
                        label="Invite Code"
                        placeholder="Invite Code"
                        onChange={(e) => setInviteCode(e.target.value)}
                        sx={{
                            backgroundColor: 'black',
                            margin: 2.5,
                            marginTop: 4,
                            width: '95%'
                        }}
                    />
                    <Stack spacing={1} direction='row-reverse'>
                        <Button variant='outlined' color={'success'} onClick={handleSubmit} sx={{ width: 80 }}>Go</Button>
                        <Button variant='outlined' color={'error'} onClick={handleClose} sx={{ width: 80 }}>Cancel</Button>
                    </Stack>
                </Box>
            </Modal>
        </Box>
    );
}

export default JoinLeagueByCodeButton;