import { useState } from 'react';
import PropTypes from 'prop-types';


import Box from '@mui/material/Box';
import Paper from '@mui/material/Paper';
import Select from 'react-select';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Typography from '@mui/material/Typography';
import { styled } from '@mui/material';



// returns a component that allows users to see picks by week for everyone in the league
const LeagueOverview = ({ picks }) => {

  const [weeklyPicks, setWeeklyPicks] = useState([]);

  // style for the react-select week chooser
  const customStyles = {
    control: (provided) => ({
      ...provided,
      width: '100px',
      backgroundColor: '#F8F8F8',
    })
  };

  // styles for the table rows
  const StyledTableRow = styled(TableRow)(() => ({
    '&:nth-of-type(odd)': {
      backgroundColor: "#1C2541",
    },
    '&:nth-of-type(even)': {
      backgroundColor: "#242f53",
    },
  }));


  function generateWeekOptions() {
    let options = [{ value: 0, label: 'Select...' }];

    for (let i = 1; i <= 18; i++) {
      options.push({ value: i, label: i.toString() });
    }

    return options;
  }

  function getPicksByWeek(week, picks) {
    const picksByWeek = picks.filter(pick => pick.week === week);

    let results = [];

    picksByWeek.forEach(pick => {
      const user = pick.owner.username;

      const index = results.findIndex(item => item.user === user);

      if (index === -1) {
        results.push({
          user: user,
          valueOne: "-",
          valueThree: "-",
          valueFive: "-"
        });
      }

      if (pick.competitor) {
        const team = pick.competitor.team.abbreviation;

        if (results[index] && pick.value === 1) {
          results[index].valueOne = team;
        } else if (results[index] && pick.value === 3) {
          results[index].valueThree = team;
        } else if (results[index] && pick.value === 5) {
          results[index].valueFive = team;
        }
      }
    });

    // sorting the array by username in alphabetical order
    results.sort((a, b) => a.user.localeCompare(b.user));
    console.log(results)
    return results;
  }

  return (
    <Box width={"100%"} sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
      <Box sx={{
        display: 'flex',
        flexDirection: 'row',
        alignItems: 'center',
      }}
      >
        <Typography variant='h6' sx={{ mr: 3 }}>Week: </Typography>
        <Select
          className='week'
          name={"week"}
          isSearchable={false}
          options={generateWeekOptions()}
          styles={customStyles}
          onChange={(choice) => setWeeklyPicks(getPicksByWeek(choice.value, picks))}
          theme={(theme) => ({
            ...theme,
            colors: {
              ...theme.colors,
              primary: '#1C2541',
              primary25: '#1C2541',
              neutral0: '#1C2541',
              neutral20: '#0B132B',
              neutral40: 'black',
              neutral50: 'black',
            },
          })}
        />
      </Box>
      <Box width={'100%'} mb={'80px'}>
        <TableContainer component={Paper} sx={{ marginTop: '20px' }}>
          <Table size='small' sx={{ width: '100%' }}>
            <TableHead>
              <TableRow>
                <TableCell sx={{ padding: '6px', maxWidth: '30vw' }}>User</TableCell>
                <TableCell sx={{ padding: '6px' }}>1 Pt</TableCell>
                <TableCell sx={{ padding: '6px' }}>3 Pts</TableCell>
                <TableCell sx={{ padding: '6px' }}>5 Pts</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {weeklyPicks && weeklyPicks.map((pick) => {
                return (
                  <StyledTableRow key={pick.user}>
                    <TableCell sx={{ pl: 1, pr: 1, maxWidth: '30vw' }}><Typography variant='body1' noWrap={true} >{pick.user}</Typography></TableCell>
                    <TableCell>{pick.valueOne}</TableCell>
                    <TableCell>{pick.valueThree}</TableCell>
                    <TableCell>{pick.valueFive}</TableCell>
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
