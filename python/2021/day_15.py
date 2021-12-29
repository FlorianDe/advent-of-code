from collections import defaultdict
from heapq import heappop, heappush
from typing import Callable, Optional, Generator

from utils.file_utils import get_input_lines
from utils.math_utils import Point2D
from utils.measurements import timed

DistanceFn = Callable[[Point2D, Point2D], float]
NeighbourFn = Callable[[Point2D], Generator[Point2D, None, None]]

locations = [[int(pos) for pos in location_row.strip()] for location_row in get_input_lines()]
adjacents: list[Point2D] = [Point2D(x, y) for x, y in [(0, 1), (1, 0), (0, -1), (-1, 0)]]
max_height: int = len(locations)
max_width: int = len(locations[0])


def get_valid_neighbours_fn(max_height: int, max_width: int):
    def valid_neighbours(current: Point2D):
        for direction in adjacents:
            p = current + direction
            if 0 <= p.y < max_height and 0 <= p.x < max_width:
                yield p

    return valid_neighbours


def manhattan_dist(a: Point2D, b: Point2D) -> float:
    return abs(a.x - b.x) + abs(a.y - b.y)


def get_weight_fn(torus: bool = False):
    def get_weight(p: Point2D, current: Optional[Point2D] = None):
        if not torus:
            if not (0 <= p.y < max_height and 0 <= p.x < max_width):
                raise ValueError("Cannot retrieve a weight for a point outside of the grid.")
            return locations[p.y][p.x]

        risk_enhancement = p.x // max_width + p.y // max_height
        return ((locations[p.y % max_height][p.x % max_width] + risk_enhancement - 1) % 9) + 1

    return get_weight


def a_star(
        start: Point2D,
        end: Point2D,
        heuristic: DistanceFn = manhattan_dist,
        tile_weight: DistanceFn = get_weight_fn(),
        valid_neighbours: NeighbourFn = get_valid_neighbours_fn(max_height, max_width),
) -> list[Point2D]:
    def reconstruct_path(came_from: dict[Point2D, Point2D], current: Point2D):
        total_path = [current]
        while current in came_from.keys():
            current = came_from[current]
            total_path.insert(0, current)
        return total_path

    seen: set[Point2D] = set()
    open_set = []
    heappush(open_set, (heuristic(start, end), start))
    came_from: dict[Point2D, Point2D] = {}
    g_score: dict[Point2D, float] = defaultdict(lambda: float('inf'))
    f_score: dict[Point2D, float] = defaultdict(lambda: float('inf'))
    g_score[start] = 0
    f_score[start] = heuristic(start, end)

    while open_set:
        current_score, current = heappop(open_set)
        if current == end:
            return reconstruct_path(came_from, current)

        for neighbour in valid_neighbours(current):
            tentative_g_score = g_score[current] + tile_weight(neighbour, current)
            if tentative_g_score < g_score[neighbour]:
                came_from[neighbour] = current
                g_score[neighbour] = tentative_g_score
                f_score[neighbour] = tentative_g_score + heuristic(neighbour, end)
                if neighbour not in seen:
                    seen.add(neighbour)
                    heappush(open_set, (f_score[neighbour], neighbour))

    return []


def solve(max_x: int, max_y: int, torus: bool = False):
    start = Point2D(0, 0)
    end = Point2D(max_x - 1, max_y - 1)
    weight_fn = get_weight_fn(torus)
    neighbours_fn = get_valid_neighbours_fn(max_x, max_y)
    return sum(weight_fn(p) for p in a_star(start, end, manhattan_dist, weight_fn, neighbours_fn) if p != start)


@timed("Part01:")
def part01():
    return solve(max_width, max_height)


@timed("Part02:")
def part02():
    return solve(5 * max_width, 5 * max_height, True)


if __name__ == '__main__':
    part01()
    part02()
