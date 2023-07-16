// External Libraries
import Avatar from '@mui/material/Avatar';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Container from '@mui/material/Container';
import Grid from '@mui/material/Grid';
import Link from '@mui/material/Link';
import LockOpenIcon from '@mui/icons-material/LockOpen';
import TextField from '@mui/material/TextField';
import Typography from '@mui/material/Typography';
import { useContext, useState } from 'react';
import { useNavigate } from 'react-router-dom';

// Contexts
import { UserContext } from '../../contexts/UserContext';

export default function RegisterForm() {
  // Hooks
  const navigate = useNavigate();
  const {signUp} = useContext(UserContext);
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [errorMessage, setErrorMessage] = useState('');

  // Functions
  const handleSubmit = async (event) => {
    event.preventDefault();

    await signUp(username, email, password).then(()=> {
      navigate('/dashboard');
    }).catch((e) => {
      console.log(e);
      setErrorMessage(e.toString());
    })
  }

  // Render
  return (
      <Container component="main" maxWidth="xs">
        <Box
            minHeight={'100vh'}
            sx={{
              marginTop: 8,
              display: 'flex',
              flexDirection: 'column',
              alignItems: 'center',
            }}
        >
          <Avatar sx={{ m: 1, bgcolor: 'primary', width: 56, height: 56 }}>
            <LockOpenIcon sx={{ fontSize: "30px" }} />
          </Avatar>
          <Typography component="h1" variant="h5">
            Register New User
          </Typography>
          <Box component="form" onSubmit={handleSubmit} noValidate sx={{ mt: 1 }}>
            <TextField
                margin="normal"
                required
                fullWidth
                id="username"
                label="Username"
                name="username"
                autoComplete="username"
                autoFocus
                value={username}
                onChange={(e) => setUsername(e.target.value)}
            />
            <TextField
                margin="normal"
                required
                fullWidth
                id="email"
                label="email"
                name="email"
                autoComplete="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
            />
            <TextField
                margin="normal"
                required
                fullWidth
                name="password"
                label="Password"
                type="password"
                id="password"
                autoComplete="current-password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
            />
            {errorMessage && <div>{errorMessage}</div>}
            <Button
                type="submit"
                fullWidth
                variant="contained"
                sx={{ mt: 3, mb: 2 }}
            >
              Register
            </Button>
            <Grid container>
              <Grid item>
                <Link onClick={() => navigate('/login')} variant="body1" sx={{ '&:hover': { cursor: "pointer" } }}>
                  {"Already have an account? Sign in."}
                </Link>
              </Grid>
            </Grid>
          </Box>
        </Box>
      </Container>
  );
}
