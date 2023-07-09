import { Accordion, AccordionDetails, AccordionSummary, Box, Button, ClickAwayListener, Stack, Tooltip, Typography } from '@mui/material'
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import ContentCopyIcon from '@mui/icons-material/ContentCopy';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import { useNavigate } from 'react-router-dom';
import PropTypes from 'prop-types';
import { useState } from 'react';
import JoinLeagueButton from '../Buttons/JoinLeagueButton';
import RulesButton from '../Buttons/RulesButton';
import DeleteLeagueButton from '../Buttons/DeleteLeagueButton';
import LeaveLeagueButton from '../Buttons/LeaveLeagueButton';

const LeagueDetails = ({ isMember, isOwner, leagueDetails }) => {
  const navigate = useNavigate();
  const [openCopied, setOpenCopied] = useState(false);



  const handleTooltipClose = () => {
    setOpenCopied(false);
  };

  const handleTooltipOpen = () => {
    setOpenCopied(true);
    navigator.clipboard.writeText(leagueDetails.inviteCode);
  };

  return (
    <Box
      maxWidth={600}
      sx={{
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        justifyContent: "center",
        pr: 1,
        pl: 1,
        margin: 'auto'
      }}
    >
      <Box
        m={3}
        p={2}
        width={'100%'}
        sx={{
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          justifyContent: "center",
          borderRadius: 2,
          bgcolor: "#1C2541"
        }}
      >
        <Typography textAlign={'center'} variant='h6' fontSize={'16'}>League Name: <Box fontSize={30} m={1}>{leagueDetails?.leagueName}</Box></Typography>
        <Stack
          direction="row"
          sx={{
            width: '92%',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            flexWrap: 'wrap',
            m: 1
          }}
        >
          {(isOwner || isMember)
            ?
            <>
              <Button variant="outlined" onClick={() => navigate(-1)} size='small' sx={{ width: 125, m: 1, borderWidth: 2, '&:hover': { borderWidth: '2px' } }}>Back<ArrowBackIcon sx={{ ml: 2 }} /></Button>
            </>
            :
            <>
              {(leagueDetails?.maxUsers > leagueDetails?.userCount) && <JoinLeagueButton width={125} size={'small'} leagueDetails={leagueDetails} />}
              <Button variant="outlined" onClick={() => navigate(-1)} size='small' sx={{ width: 125, m: 1, borderWidth: 2, '&:hover': { borderWidth: '2px' } }}>Back<ArrowBackIcon sx={{ ml: 2 }} /></Button>
            </>
          }
        </Stack>
      </Box>
      <Accordion disableGutters={true} sx={{
        width: '100%'
      }}>
        <AccordionSummary
          expandIcon={<ExpandMoreIcon />}
          id="leagueDetailsAccordion"
        >
          <Typography sx={{ fontSize: 18 }}>League Details</Typography>
        </AccordionSummary>
        <AccordionDetails>
          <Stack direction={'row'}>
            <RulesButton variant={'outlined'} size={'small'} width={125} margin={8} />
          </Stack>
          <Box>
            <Typography variant={'body1'} sx={{ fontSize: 18, mt: 2, mb: 1 }}>Commissioner: {leagueDetails?.leagueOwner}</Typography>
            <Typography variant={'body1'} sx={{ fontSize: 18, mb: 1 }}>Members: {leagueDetails?.userCount} / {leagueDetails?.maxUsers}</Typography>
            <Typography variant={'body1'} sx={{ fontSize: 18, mb: 1 }}>Availability: {leagueDetails?.isPrivate ? "Private" : "Public"} </Typography>
            {(isOwner || !leagueDetails?.isPrivate) &&
              <Box>
                {/* tooltip and listener for the copying the invite code */}
                <ClickAwayListener onClickAway={handleTooltipClose}>
                  <div>
                    <Tooltip
                      PopperProps={{
                        disablePortal: true,
                      }}
                      onClose={handleTooltipClose}
                      open={openCopied}
                      disableFocusListener
                      disableHoverListener
                      leaveTouchDelay={1000}
                      leaveDelay={1000}
                      title="Copied!"
                      sx={{
                        color: 'red'
                      }}
                    >
                      <Typography variant={'body1'} onClick={handleTooltipOpen} sx={{ fontSize: 18, mb: 3 }}>Invite Code: {leagueDetails?.inviteCode} <ContentCopyIcon sx={{ ml: 1, '&:hover': { cursor: 'pointer' } }} /></Typography>
                    </Tooltip>
                  </div>
                </ClickAwayListener>
              </Box>
            }
            {isOwner && <DeleteLeagueButton leagueDetails={leagueDetails} />}
            {isMember && <LeaveLeagueButton leagueDetails={leagueDetails} />}
          </Box>
        </AccordionDetails>
      </Accordion>
    </Box>

  )
}

LeagueDetails.propTypes = {
  isMember: PropTypes.bool,
  isOwner: PropTypes.bool,
  leagueDetails: PropTypes.object
}

export default LeagueDetails;