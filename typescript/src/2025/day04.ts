import { AocDay, AocDayDecorator, SolutionParts } from '../aoc';
import { DiagonalGridDirections, VerticalAndHorizontalGridDirections } from '../aoc/math';

@AocDayDecorator('2025', '4')
export class Day04 extends AocDay {
  solveImpl(): SolutionParts {
    const paperRollsGrid = this.input.split('\n').map((row) => row.split(''));

    const removeRolls = (initialGrid: string[][], iterations: number = Number.MAX_SAFE_INTEGER) => {
      const grid = structuredClone(initialGrid);
      const TOILET_PAPER_PLACEHOLDER = '@' as const;
      const TOILET_PAPER_REMOVED_PLACEHOLDER = 'x' as const;
      const directions = [...DiagonalGridDirections, ...VerticalAndHorizontalGridDirections];

      let totalRemovals = 0;
      for (let i = 0; i < iterations; i++) {
        const removals: { row: number; col: number }[] = [];
        for (let row = 0; row < grid.length; row++) {
          for (let col = 0; col < grid[row].length; col++) {
            if (grid[row][col] === TOILET_PAPER_PLACEHOLDER) {
              let neighbors = 0;
              for (let k = 0; k < directions.length; k++) {
                const r = row + directions[k].y;
                const c = col + directions[k].x;
                if (0 <= r && r < grid.length && 0 <= c && c < grid[row].length && grid[r][c] === TOILET_PAPER_PLACEHOLDER) {
                  neighbors++;
                }
              }
              if (neighbors < 4) {
                removals.push({ row, col });
              }
            }
          }
        }
        for (const { row, col } of removals) {
          grid[row][col] = TOILET_PAPER_REMOVED_PLACEHOLDER;
        }
        if (removals.length === 0) {
          return totalRemovals;
        }
        totalRemovals += removals.length;
      }
      return totalRemovals;
    };

    return {
      part1: removeRolls(paperRollsGrid, 1),
      part2: removeRolls(paperRollsGrid),
    };
  }
}
