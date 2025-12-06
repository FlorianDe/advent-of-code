import { Aggregates, AocDay, AocDayDecorator, SolutionParts } from '../aoc';

@AocDayDecorator('2025', '2')
export class Day02Regex extends AocDay {
  solveImpl(): SolutionParts {
    const getInvalidIdsSum = (regex: RegExp) =>
      this.input
        .split(',')
        .map((rangeStr) => {
          const [start, end] = rangeStr.split('-').map((s) => Number.parseInt(s, 10));
          return Array.from({ length: end - start + 1 }, (_, i) => start + i)
            .filter((n) => regex.test(`${n}`))
            .reduce(Aggregates.sum, 0);
        })
        .reduce(Aggregates.sum, 0);

    return {
      part1: getInvalidIdsSum(/^(\d+)\1$/),
      part2: getInvalidIdsSum(/^(\d+)\1+$/),
    };
  }
}

console.log(new Day02Regex().solve());
