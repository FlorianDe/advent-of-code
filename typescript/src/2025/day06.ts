import { Aggregates, AocDay, AocDayDecorator, Arrays, SolutionParts } from '../aoc';

@AocDayDecorator('2025', '6')
export class Day06 extends AocDay {
  solveImpl(): SolutionParts {
    const OperationMap = {
      '+': { fn: Aggregates.sum, initial: 0 },
      '*': { fn: Aggregates.mul, initial: 1 },
    };

    type Exercise = { operands: number[]; operator: keyof typeof OperationMap };

    const calculate = (exercises: Array<Exercise>) =>
      exercises
        .map(({ operands, operator }) => operands.reduce(OperationMap[operator].fn, OperationMap[operator].initial))
        .reduce(Aggregates.sum);

    const readLikeHuman = (text: string): Exercise[] =>
      Arrays.transpose(text.split('\n').map((row) => row.trim().split(/\s+/))).map<Exercise>((exerciseRow) => ({
        operands: exerciseRow.slice(0, -1).map((n) => Number.parseInt(n, 10)),
        operator: exerciseRow.at(-1)!! as keyof typeof OperationMap,
      }));

    const readLikeCephalopod = (text: string): Exercise[] => {
      const rows = text.split('\n').map((r) => r.split(''));
      const nCols = Math.max(...rows.map((r) => r.length));
      rows.forEach((r) => {
        while (r.length < nCols) r.push(' ');
      });

      const problems: Exercise[] = [];
      for (let col = 0; col < nCols; ) {
        if (!rows.every((r) => r[col] === ' ')) {
          const start = col;
          while (col < nCols) {
            const currentCol = col;
            if (rows.some((r) => r[currentCol] !== ' ')) {
              col++;
            } else {
              break;
            }
          }
          const end = col;
          const operands: number[] = [];
          for (let c = start; c < end; c++) {
            const digits = rows
              .slice(0, rows.length - 1)
              .map((r) => r[c])
              .join('')
              .trim();

            operands.push(Number(digits));
          }

          problems.push({
            operands,
            operator: rows[rows.length - 1].slice(start, end).find((ch) => ch !== ' ') as keyof typeof OperationMap,
          });
        }
        col++;
      }

      return problems;
    };

    return {
      part1: calculate(readLikeHuman(this.input)),
      part2: calculate(readLikeCephalopod(this.input)),
    };
  }
}
