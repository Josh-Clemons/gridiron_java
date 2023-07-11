export function getSelectOptions(competitors, week, team) {
    let options = [];

    const currentDate = new Date();

    // TODO when the data updates, change the < to a >. I have it this way for testing
    competitors && competitors
        .filter(c => c.week === week && new Date(c.startDate) < currentDate)
        .map(c => {
            options.push({value: c.team.abbreviation, label: c.team.abbreviation, isDisabled: false})
        });

    options = options.sort((a, b) => a.label.localeCompare(b.label));

    // Check if the team exists in options
    const teamExists = options.some(option => option.value === team);

    // If it doesn't exist, add it
    if (!teamExists) {
        options.push({value: team, label: team, isDisabled: false});
    }

    // options.unshift({value: '', label: 'Select...', isDisabled: false})


    return options;
}