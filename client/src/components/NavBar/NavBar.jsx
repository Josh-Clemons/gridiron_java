import * as React from 'react';
import { useNavigate } from 'react-router-dom';
import { Toolbar, IconButton, Typography, Stack } from '@mui/material';

import AddCircleIcon from '@mui/icons-material/AddCircle';
import Box from '@mui/material/Box';
import Drawer from '@mui/material/Drawer';
import Button from '@mui/material/Button';
import List from '@mui/material/List';
import Divider from '@mui/material/Divider';
import GridViewIcon from '@mui/icons-material/GridView';
import HomeIcon from '@mui/icons-material/Home';
import ListItem from '@mui/material/ListItem';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import MenuIcon from '@mui/icons-material/Menu';
import SearchIcon from '@mui/icons-material/Search';
import { UserContext } from '../../contexts/UserContext';



export default function NavBar() {

    const navigate = useNavigate();
    const {user} = React.useContext(UserContext);

    const [state, setState] = React.useState({ left: false });

    const logout = () => {
        navigate('/dashboard');
    }

    const toggleDrawer = (open) =>
        (event) => {
            if (
                event.type === 'keydown' &&
                ((event).key === 'Tab' ||
                    (event).key === 'Shift')
            ) {
                return;
            }
            setState({ left: open });
        };

    const list = () => (
        <Box
            sx={{ width: 250 }}
            role="presentation"
            onClick={toggleDrawer(false)}
            onKeyDown={toggleDrawer(false)}
        >
            <List>
                <ListItem disablePadding>
                    <ListItemButton onClick={() => navigate('/')} >
                        <ListItemIcon>
                            <HomeIcon />
                        </ListItemIcon>
                        <ListItemText primary='Home' />
                    </ListItemButton>
                </ListItem>

                {user
                    ?
                    <>
                      <ListItem disablePadding>
                          <ListItemButton onClick={() => navigate('/dashboard')} >
                              <ListItemIcon>
                                  <GridViewIcon />
                              </ListItemIcon>
                              <ListItemText primary='Dashboard' />
                          </ListItemButton>
                      </ListItem>
                      <ListItem disablePadding>
                          <ListItemButton onClick={() => navigate('/find-league')} >
                              <ListItemIcon>
                                  <SearchIcon />
                              </ListItemIcon>
                              <ListItemText primary='Find a League' />
                          </ListItemButton>
                      </ListItem>
                      <ListItem disablePadding>
                          <ListItemButton onClick={() => navigate('/create')} >
                              <ListItemIcon>
                                  <AddCircleIcon />
                              </ListItemIcon>
                              <ListItemText primary='Create a League' />
                          </ListItemButton>
                      </ListItem>
                    </>
                    :
                    null
                }

            </List>
            <Divider />

            {user ?
                <Stack spacing={1} direction="column">
                    <Button size='large' sx={{ justifyContent: 'flex-start' }} onClick={() => logout()}>
                        Logout
                    </Button>
                    <Button size='large' sx={{ justifyContent: 'flex-start' }} onClick={() => navigate('/about')}>
                        About
                    </Button>
                </Stack>
                :
                <Stack spacing={1} direction="column">
                    <Button size='large' sx={{ justifyContent: 'flex-start' }} onClick={() => navigate('/login')}>
                        Login
                    </Button>
                    <Button size='large' sx={{ justifyContent: 'flex-start' }} onClick={() => navigate('/register')}>
                        Register
                    </Button>
                    <Button size='large' sx={{ justifyContent: 'flex-start' }} onClick={() => navigate('/about')}>
                        About
                    </Button>
                </Stack>
            }
        </Box>
    );

    return (
        <div>
            <React.Fragment>
                <Toolbar>
                    <IconButton
                        color="inherit"
                        aria-label="open drawer"
                        edge="start"
                        onClick={toggleDrawer(true)}
                    >
                        <MenuIcon sx={{ fontSize: "35px" }} />
                    </IconButton>
                    <Typography variant="h5" noWrap component="div">
                        Grid Iron Pickem
                    </Typography>
                </Toolbar>
                <Drawer
                    anchor='left'
                    open={state['left']}
                    onClose={toggleDrawer(false)}
                >
                    {list()}
                </Drawer>
            </React.Fragment>
        </div>
    );
}