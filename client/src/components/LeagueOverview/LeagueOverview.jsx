import { useContext, useEffect, useState } from 'react';
import PropTypes from 'prop-types';


import Box from '@mui/material/Box';
import Paper from '@mui/material/Paper';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Typography from '@mui/material/Typography';
import { UserContext } from '../../contexts/UserContext';
import { StyledTableRow } from '../../styles/SharedStyles';
import { NativeSelect, FormControl } from '@mui/material';




// returns a component that allows users to see picks by week for everyone in the league
const LeagueOverview = ({ picks }) => {
  const { user } = useContext(UserContext);
  const [weeklyPicks, setWeeklyPicks] = useState([]);

  const handleWeekChange = (event) => {
    const week = Number(event.target.value);
    setWeeklyPicks(getPicksByWeek(week, picks, user));
  }

  const weekOptions = [...generateWeekOptions()].map(option =>
    <option key={option.value} value={option.value}>{option.label}</option>
  );

  function generateWeekOptions() {
    let options = [];

    for (let i = 1; i <= 18; i++) {
      options.push({ value: i, label: i.toString() });
    }

    return options;
  }

  function getPicksByWeek(week, picks, user) {
    const picksByWeek = picks.filter(pick => pick.week === week);
    let results = [];

    picksByWeek.forEach(pick => {
      const owner = pick.owner.username;
      let index = results.findIndex(item => item.owner === owner);

      if (index === -1) {
        results.push({
          owner: owner,
          valueOne: "-",
          isOneWinner: false,
          valueThree: "-",
          isThreeWinner: false,
          valueFive: "-",
          isFiveWinner: false,
          weeklyScore: 0,
          disableBorder: true
        });
        index = results.length - 1;
      }

      if (pick.competitor && pick.competitor.team) {
        const team = pick.competitor.team.abbreviation;

        // only show other players picks if now is after the start date
        const startDate = new Date(pick.competitor.startDate)
        const now = new Date();
        if (now > startDate || pick.owner.username === user.username) {
          results[index].disableBorder = false;
          if (results[index] && pick.value === 1) {
            results[index].valueOne = team;
            results[index].isOneWinner = pick.competitor.winner;
            pick.competitor.winner && results[index].weeklyScore++;
          } else if (results[index] && pick.value === 3) {
            results[index].valueThree = team;
            results[index].isThreeWinner = pick.competitor.winner;
            pick.competitor.winner && (results[index].weeklyScore += 3);
          } else if (results[index] && pick.value === 5) {
            results[index].valueFive = team;
            results[index].isFiveWinner = pick.competitor.winner;
            pick.competitor.winner && (results[index].weeklyScore += 5);
          }
        }
      }
    });

    // add 2 bonus points if a user has all 3 selections correct in the given week
    for (let result of results) {
      if (result.isOneWinner && result.isThreeWinner && result.isFiveWinner) {
        result.weeklyScore += 2;
      }
    }

    // sorting the array by username in alphabetical order
    results.sort((a, b) => a.owner.localeCompare(b.owner));
    console.log(results)
    return results;
  }

  useEffect(() => {
    setWeeklyPicks(getPicksByWeek(1, picks, user));
  }, [picks, user])

  return (
    <Box width={"100%"} sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
      <Box sx={{
        display: 'flex',
        flexDirection: 'row',
        alignItems: 'center',
      }}
      >
        <Typography variant='h6' sx={{ mr: 3 }}>Week: </Typography>
        <FormControl variant="filled" style={{ width: '100px' }}>
          <NativeSelect
            onChange={handleWeekChange}
          >
            {weekOptions}
          </NativeSelect>
        </FormControl>
      </Box>
      <Box width={'100%'} mb={'80px'} >
        <TableContainer component={Paper} sx={{ marginTop: '20px' }}>
          <Table size='small' sx={{ width: '100%' }}>
            <TableHead>
              <TableRow>
                <TableCell sx={{ padding: '6px', maxWidth: '30vw' }}>User</TableCell>
                <TableCell sx={{ padding: '6px', pl: 1 }}>Score</TableCell>
                <TableCell sx={{ padding: '6px', pl: 3 }}>1 Pt</TableCell>
                <TableCell sx={{ padding: '6px', pl: 3 }}>3 Pts</TableCell>
                <TableCell sx={{ padding: '6px', pl: 3 }}>5 Pts</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {weeklyPicks && weeklyPicks.map((pick) => {
                return (
                  <StyledTableRow key={pick.owner}>
                    <TableCell sx={{ pl: 1, pr: 1, maxWidth: '30vw' }}><Typography variant='body1' noWrap={true} >{pick.owner}</Typography></TableCell>
                    <TableCell>{pick.weeklyScore}</TableCell>
                    <TableCell>
                      <Box
                        border={
                          (pick.disableBorder || pick.valueOne === '-')
                            ? "none"
                            : pick.isOneWinner
                              ? "1px solid green"
                              : "1px solid darkred"
                        }
                        borderRadius={1}
                        p={.5}
                      >
                        {pick.valueOne}
                      </Box>
                    </TableCell>
                    <TableCell>
                      <Box
                        border={
                          (pick.disableBorder || pick.valueOne === '-')
                            ? "none"
                            : pick.isThreeWinner
                              ? "1px solid green"
                              : "1px solid darkred"
                        }
                        borderRadius={1}
                        p={.5}
                      >
                        {pick.valueThree}
                      </Box>
                    </TableCell>
                    <TableCell>
                      <Box
                        border={
                          (pick.disableBorder || pick.valueOne === '-')
                            ? "none"
                            : pick.isFiveWinner
                              ? "1px solid green"
                              : "1px solid darkred"
                        }
                        borderRadius={1}
                        p={.5}
                      >
                        {pick.valueFive}
                      </Box>
                    </TableCell>
                  </StyledTableRow>
                )
              })}
            </TableBody>
          </Table>
        </TableContainer>
      </Box>
    </Box>
  )
}

LeagueOverview.propTypes = {
  picks: PropTypes.array
}

export default LeagueOverview;
