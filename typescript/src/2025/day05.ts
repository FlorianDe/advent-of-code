import { Aggregates, AocDay, AocDayDecorator, SolutionParts } from '../aoc';

@AocDayDecorator('2025', '5')
export class Day05 extends AocDay {
  solveImpl(): SolutionParts {
    type Range = { start: number; end: number };

    const parseInput = () => {
      const [rangesSection, idsSection] = this.input.split('\n\n');

      const ranges = rangesSection.split('\n').map((row) => {
        const [start, end] = row
          .trim()
          .split('-')
          .map((s) => Number.parseInt(s, 10));
        return { start, end };
      });

      const ids = idsSection.split('\n').map((id) => Number.parseInt(id.trim(), 10));

      return { ranges, ids };
    };

    function mergeRanges(ranges: Range[]): Range[] {
      if (ranges.length === 0) return [];
      ranges.sort((a, b) => a.start - b.start);

      const merged: Range[] = [];
      let curr: Range = { ...ranges[0] };

      for (let i = 1; i < ranges.length; i++) {
        const r = ranges[i];

        if (r.start <= curr.end) {
          curr.end = Math.max(curr.end, r.end);
        } else {
          merged.push(curr);
          curr = { ...r };
        }
      }
      merged.push(curr);
      return merged;
    }

    const { ranges, ids } = parseInput();

    return {
      part1: ids.filter((id) => ranges.some(({ start, end }) => start <= id && id <= end)).length,
      part2: mergeRanges(ranges)
        .map(({ start, end }) => end - start + 1)
        .reduce(Aggregates.sum),
    };
  }
}
