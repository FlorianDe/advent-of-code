import { Aggregates, AocDay, AocDayDecorator, SolutionParts } from '../aoc';

@AocDayDecorator('2025', '2')
export class Day02 extends AocDay {
  solveImpl(): SolutionParts {
    type Range = { start: number; end: number };
    type GetMaxRepetitionsFn = (endLen: number) => number;

    const ranges = this.input.split(',').map((rangeStr) => {
      const [start, end] = rangeStr.split('-').map((s) => Number.parseInt(s, 10));
      return { start, end };
    });

    const getInvalidIds =
      (getMaxRepetitions: GetMaxRepetitionsFn) =>
      ({ start, end }: Range) => {
        const invalidIds = new Set<number>();
        const endLen = `${end}`.length;
        for (let reps = 2; reps <= getMaxRepetitions(endLen); reps++) {
          const nLength = Math.floor(endLen / reps);
          const minN = 10 ** (nLength - 1);
          const maxN = 10 ** nLength - 1;
          for (let n = minN; n <= maxN; n++) {
            const repeatedNumber = Number(`${n}`.repeat(reps));
            if (repeatedNumber > end) break;
            if (repeatedNumber >= start) invalidIds.add(repeatedNumber);
          }
        }
        return invalidIds;
      };

    const getInvalidIdsSum = (getMaxRepetitions: GetMaxRepetitionsFn) =>
      ranges
        .map(getInvalidIds(getMaxRepetitions))
        .map((ids) => Array.from(ids).reduce(Aggregates.sum, 0))
        .reduce(Aggregates.sum);

    return {
      part1: getInvalidIdsSum(() => 2),
      part2: getInvalidIdsSum((endLen) => endLen),
    };
  }
}
