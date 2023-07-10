import { useContext, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import AddCircleIcon from '@mui/icons-material/AddCircle';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Container from '@mui/material/Container';
import FormControl from '@mui/material/FormControl';
import FormHelperText from '@mui/material/FormHelperText';
import TextField from '@mui/material/TextField';
import Typography from '@mui/material/Typography';
import { Switch } from '@mui/material';
import axios from 'axios';
import { UserContext } from '../contexts/UserContext';
import { successAlert } from '../utils/ToastAlerts';


const CreateLeaguePage = () => {
    const navigate = useNavigate();
    const { user } = useContext(UserContext);

    const [leagueName, setLeagueName] = useState('');
    const [isPrivate, setIsPrivate] = useState(false);
    const [maxUsers, setMaxUsers] = useState(100);
    const [error, setError] = useState(false);

    useEffect(() => {
        if (maxUsers < 2 || maxUsers > 100) {
            setError(true);
        } else {
            setError(false);
        }
    }, [maxUsers]);

    const handleSubmit = async (event) => {
        event.preventDefault();
        await axios.post('http://localhost:8080/api/league/create', {
            leagueName: leagueName,
            maxUsers: maxUsers,
            isPrivate: isPrivate
        }, {
            headers: {
                'Authorization': `Bearer ${user.accessToken}`
            }
        }).then(() => {
            successAlert('League created');
            navigate('/dashboard');
        })
    };
    const handleUserChange = (event) => {
        setMaxUsers(event.target.value);
    };

    const handleIsPrivate = (event) => {
        setIsPrivate(event.target.checked)
    }

    return (
        <Container
            sx={{
                borderRadius: 2,
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                justifyContent: "flex-start",
                padding: "10px",
                textAlign: "center",
                maxWidth: 600,
                minHeight: '100vh'
            }}
        >
            <Typography variant="h4">Create a New League</Typography>
            <Box component="form" onSubmit={handleSubmit} sx={{
                display: 'flex',
                flexDirection: 'column',
                justifyContent: 'center'
            }}>
                <TextField
                    required
                    id="leagueName"
                    label="League Name"
                    placeholder="League Name"
                    value={leagueName}
                    onChange={(e) => setLeagueName(e.target.value)}
                    sx={{
                        backgroundColor: 'black',
                        margin: 2.5,
                        marginTop: 4
                    }}
                />
                <FormControl error={error}>
                    <TextField
                        type="number"
                        value={maxUsers}
                        onChange={handleUserChange}
                        inputProps={{
                            min: 2,
                            max: 100
                        }}
                    />
                    {error && <FormHelperText>Value must be between 2 and 100</FormHelperText>}
                </FormControl>
                <Box sx={{ display: 'flex', flexDirection: 'row', justifyContent: 'space-around', alignItems: 'center', mb: 2 }}>
                    <Typography variant='body1'>Set to Private:</Typography>
                    <Switch checked={isPrivate} onClick={handleIsPrivate} />
                    <Typography variant='body1' width={30}>{isPrivate ? 'Yes' : 'No'}</Typography>
                </Box>
                <Button variant="outlined" color="warning" type="submit" disabled={error} sx={{ width: "250px", marginTop: "10px", marginBottom: "8px", borderWidth: 2, '&:hover': { borderWidth: '2px' } }}>Create<AddCircleIcon sx={{ ml: 2 }} /></Button>
            </Box>
            <Button variant="outlined" onClick={() => navigate('/dashboard')} sx={{ width: "250px", position: "fixed", bottom: 60, borderWidth: 2, '&:hover': { borderWidth: '2px' } }}>My Leagues<ArrowBackIcon sx={{ ml: 2 }} /></Button>
        </Container>
    )
}

export default CreateLeaguePage;