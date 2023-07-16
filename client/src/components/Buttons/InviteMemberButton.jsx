// Libraries
import { useContext, useState } from 'react';
import { useParams } from 'react-router-dom';

// UI Components
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Modal from '@mui/material/Modal';
import Stack from '@mui/material/Stack';
import Typography from '@mui/material/Typography';
import TextField from '@mui/material/TextField';

// Contexts
import { UserContext } from "../../contexts/UserContext.jsx";

// Utilities
import { errorAlert } from "../../utils/ToastAlerts.js";
import { inviteNewMember } from "../../utils/api.js";

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

const ModalEmailInvite = () => {
    const { user } = useContext(UserContext);
    const { inviteCode } = useParams();

    const [open, setOpen] = useState(false);
    const [emailAddress, setEmailAddress] = useState('');

    const handleOpen = () => setOpen(true);
    const handleClose = () => setOpen(false);

    const handleSubmit = (event) => {
        event.preventDefault();

        if (emailAddress !== '') {
            inviteNewMember(emailAddress, inviteCode, user.accessToken);
            setEmailAddress("");
            handleClose();
        } else {
            errorAlert("Enter a valid email")
        }
    };

    return (
        <Box>
            <Typography
                fontSize={18}
                onClick={handleOpen}
                color={'primary'}
                sx={{width: 250, mb: 1, mt: 1, '&:hover': {cursor: 'pointer'}}}
            >
                Invite Friend by Email
            </Typography>
            <Modal
                open={open}
                onClose={handleClose}
                aria-labelledby="modal-modal-title"
                aria-describedby="modal-modal-description"
            >
                <Box sx={style} component='form' onSubmit={handleSubmit}>
                    <Typography id="modal-modal-title" variant="h6" component="h2">
                        Enter email address to invite:
                    </Typography>
                    <TextField
                        required
                        id="inviteEmailAddress"
                        label="Email Address"
                        placeholder="example@gmail.com"
                        autoFocus
                        value={emailAddress}
                        onChange={(e) => setEmailAddress(e.target.value)}
                        sx={{
                            backgroundColor: 'black',
                            margin: 2.5,
                            marginTop: 4,
                            width: '95%'
                        }}
                    />
                    <Stack spacing={1} direction='row-reverse'>
                        <Button
                            variant='outlined'
                            color={'success'}
                            onClick={handleSubmit}
                            sx={{width: 80}}
                        >
                            Send
                        </Button>
                        <Button
                            variant='outlined'
                            color={'error'}
                            onClick={handleClose}
                            sx={{width: 80}}
                        >
                            Cancel
                        </Button>
                    </Stack>
                </Box>
            </Modal>
        </Box>
    );
}

export default ModalEmailInvite;