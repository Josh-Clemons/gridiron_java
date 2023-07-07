export function getSelectOptions(competitors, week) {
  let options = [];

  const currentDate = new Date();

  // TODO when the data updates, change the < to a >. I have it this way for testing
  competitors && competitors
    .filter(c => c.week === week && new Date(c.startDate) < currentDate)
    .map(c => {
      options.push({ value: c.team.abbreviation, label: c.team.abbreviation, isDisabled: false })
    });

  options = options.sort((a, b) => a.label.localeCompare(b.label));

  options.unshift({ value: '', label: 'Select...', isDisabled: false })

  return options;
}