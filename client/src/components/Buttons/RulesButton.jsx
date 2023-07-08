/* eslint-disable react/no-unescaped-entities */
import React from 'react'
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import GavelIcon from '@mui/icons-material/Gavel';
import Typography from '@mui/material/Typography';
import Stack from '@mui/material/Stack';
import Modal from '@mui/material/Modal';
import PropTypes from 'prop-types';


const style = {
    position: 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: '95%',
    maxWidth: 600,
    bgcolor: 'background.paper',
    border: '2px solid #000',
    boxShadow: 24,
    p: 2,
};

const RulesButton = ({ size, width, variant, margin }) => {

    const [open, setOpen] = React.useState(false);
    const handleOpen = () => setOpen(true);
    const handleClose = () => { setOpen(false) };


    // the rules do not match Mark's original rules exactly, mostly because there is additional functionality
    // that would need to be implemented for those rules to apply
    return (
        <Box>
            <Button variant={variant} onClick={handleOpen} color={'secondary'} size={size} sx={{ width: { width }, margin: { margin }, borderWidth: 2, '&:hover': { borderWidth: '2px' } }}>Rules<GavelIcon sx={{ ml: 2 }} /></Button>
            <Modal
                open={open}
                onClose={handleClose}
                aria-labelledby="modal-modal-title"
                aria-describedby="modal-modal-description"
            >
                <Box sx={style}>
                    <Typography id="modal-modal-title" variant="h6" component="h2">
                        Rules:
                    </Typography>
                    <Typography variant='body1' paragraph={true} style={{ width: '95%', height: '60vh', overflow: 'auto' }}>
                        Each week, you must select 3 different teams to win their games.  Do not worry about point spreads.  You only need to select wins.
                        <br />
                        <br />
                        Your 3 teams must be placed in the following categories:  5 points, 3 points and 1 point.
                        <br />
                        <br />
                        If your team wins, you will receive the points in the respective categories.
                        <br />
                        <br />
                        For example, let's say you select the IND (5), BAL (3) and GB (1).  If only the Colts and Packers win that week, you would receive 6 points (IND 5, GB 1).
                        <br />
                        <br />
                        If in any week of the season, your 3 choices result in 3 wins, you will be awarded 2 additional points (11 points total for that week).
                        <br />
                        <br />
                        Once you select a team in a point category, you cannot place them in that same point category for the remainder of the season.
                        <br />
                        <br />
                        For example, you can only select the Colts to win for 5 points one time in the season.
                        <br />
                        <br />
                        Selections must be made prior to the game time of your picks or you receive no points. There are Thursday games that will need earlier submittals.
                        <br />
                        <br />
                        Additional Rules:
                        <br />
                        -If a game results in a tie, it's considered a loss in Grid Iron
                        <br />
                        -Only one team from the same game can be selected.
                        <br />

                    </Typography>
                    <Stack spacing={1} direction='row-reverse'>
                        <Button variant='outlined' color={'primary'} onClick={handleClose} sx={{ width: 80 }}>Close</Button>
                    </Stack>
                </Box>
            </Modal>
        </Box>
    );
}

RulesButton.propTypes = {
  size : PropTypes.string,
  width : PropTypes.number,
  variant : PropTypes.string,
  margin : PropTypes.number
}

export default RulesButton;