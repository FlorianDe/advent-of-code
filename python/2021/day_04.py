from __future__ import annotations
from dataclasses import dataclass

from typing import Optional

inputs = open("inputs/day_04.txt", "r").read().split("\n\n")
numbers_input = list(map(int, inputs[0].split(",")))
boards_input = [[row.strip().split() for row in board.split("\n")] for board in inputs[1:]]


@dataclass
class Cell:
    value: int
    marked: bool = False

    def mark(self):
        self.marked = True

    @staticmethod
    def is_marked(cell: Cell):
        return cell.marked is True


class Board:
    cells: list[list[Cell]]
    numbers_drawn_count: int = 0
    min_win_count: Optional[int] = None
    num_for_min_win: Optional[int] = None

    def __init__(self, board_cells: list[list[str]]) -> None:
        super().__init__()
        self.cells = [[Cell(int(cell)) for cell in row] for row in board_cells]

    def mark_cell(self, num: int) -> bool:
        """
        Marks a cell on the board if the specified number was found.
        :param num: The number to be searched for and marked on the board
        :return: Will return true if the specified number led to the _first_ win of this board.
        """
        self.numbers_drawn_count += 1
        if self.min_win_count is None:
            for row in self.cells:
                for cell in row:
                    if cell.value == num and cell.marked is False:
                        cell.mark()
                        won = self.check_win()
                        if won and self.num_for_min_win is None:
                            self.num_for_min_win = num
                            return True
        return False

    def check_win(self) -> bool:
        if self.min_win_count is None:
            for row in self.cells:
                if self.__check_row_column_win(row):
                    return True
            for cell_idx in range(len(self.cells[0])):
                column = [row[cell_idx] for row in self.cells]
                if self.__check_row_column_win(column):
                    return True

    def __check_row_column_win(self, cells: list[Cell]) -> bool:
        if len(cells) == len(list(filter(Cell.is_marked, cells))):
            if self.min_win_count is None:
                self.min_win_count = self.numbers_drawn_count
            return True
        return False

    def score(self) -> int:
        non_marked_sum = 0
        for row in self.cells:
            for cell in row:
                if not Cell.is_marked(cell):
                    non_marked_sum += cell.value
        return non_marked_sum * self.num_for_min_win

    @staticmethod
    def create_boards(boards_input: list[list[list[str]]]) -> list[Board]:
        return [Board(board_cells) for board_cells in boards_input]


def calculate_first_last_board_score(boards: list[Board], numbers: list[int]) -> tuple[int, int]:
    min_win_board: Optional[Board] = None
    max_win_board: Optional[Board] = None
    for board in boards:
        for idx in range(len(numbers)):
            if board.mark_cell(numbers[idx]):
                numbers_drawn = idx + 1
                if min_win_board is None or min_win_board.min_win_count > numbers_drawn:
                    min_win_board = board
                if max_win_board is None or max_win_board.min_win_count < numbers_drawn:
                    max_win_board = board
                    break

    return min_win_board.score(), max_win_board.score()


if __name__ == '__main__':
    first_score, last_score = calculate_first_last_board_score(Board.create_boards(boards_input), numbers_input)
    print(f"Part01: {first_score}")
    print(f"Part01: {last_score}")
