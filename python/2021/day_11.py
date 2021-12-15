import itertools
from typing import Callable, Union
from ..utils.file_utils import get_input_lines
from ..utils.math_utils import Point2D
from ..utils.measurements import timed


locations: list[list[int]] = [[int(pos) for pos in location_row.strip()] for location_row in get_input_lines()]
adjacents: list[Point2D] = [Point2D(x, y) for x in [-1, 0, 1] for y in [-1, 0, 1]]
max_height: int = len(locations)
max_width: int = len(locations[0])

def valid_neighbours(current: Point2D):
    for direction in adjacents:
        p = current + direction
        if 0 <= p.y < max_height and 0 <= p.x < max_width:
            yield p

def simulate_octopuses(octopuses: list[list[int]], termination_fn: tuple[Callable[[int, int, int], Union[int, None]]]):
    total_flashes = 0
    for step in itertools.count(1):
        flashes = 0
        octopuses = [[col+1 for col in row] for row in octopuses]
        stack: list[Point2D] = [Point2D(cIdx, rIdx) for rIdx, row in enumerate(octopuses) for cIdx, col in enumerate(row) if col > 9]
        while stack:
            flashes += 1
            for next in valid_neighbours(stack.pop()):
                octopuses[next.y][next.x] += 1
                if octopuses[next.y][next.x] == 10:
                    stack.append(next)
        total_flashes += flashes
        octopuses = [[0 if col > 9 else col for col in row] for row in octopuses]

        termination = termination_fn(step, flashes, total_flashes)
        if termination is not None:
            return termination


@timed("Part01:")
def part01(octopuses: list[list[int]], steps: int = 100):
    def flashes_after_x_steps(step: int, flashes: int, total_flashes: int) -> Union[int, None]:
        return total_flashes if step == steps else None

    return simulate_octopuses(octopuses, flashes_after_x_steps)


@timed("Part02:")
def part02(octopuses: list[list[int]]):
    def steps_when_all_flash(step: int, flashes: int, total_flashes: int) -> Union[int, None]:
        return step if flashes == max_height*max_width else None

    return simulate_octopuses(octopuses, steps_when_all_flash)


if __name__ == '__main__':
    part01(locations)
    part02(locations)
