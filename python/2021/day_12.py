from collections import defaultdict
from typing import Callable, Union
from ..utils.file_utils import get_input_lines
from ..utils.math_utils import Point2D
from ..utils.measurements import timed

def create_adjacency_list(input_lines: list[str])-> dict[str, list[str]]:
    adjacency_list = defaultdict(list)
    for line in input_lines:
        src, dest = line.strip().split('-')
        adjacency_list[src].append(dest)
        adjacency_list[dest].append(src)
    return adjacency_list

start = 'start'
end = 'end'
cave_connections = create_adjacency_list(get_input_lines())

def traverse_caves_dfs(small_cave_max_visit, cur_cave: str = start, visited: dict[str, int] = {start: 1}, end_cave: str = end):
    if cur_cave == end_cave:
        return 1
    paths = 0
    for next_cave in cave_connections[cur_cave]:
        if next_cave.islower():
            if next_cave not in visited:
                vis_copy = visited.copy()
                vis_copy[next_cave] = 1
                paths += traverse_caves_dfs(small_cave_max_visit, next_cave, vis_copy, end_cave)
            elif max(visited.values()) < small_cave_max_visit and next_cave not in {start, end}:
                vis_copy = visited.copy()
                vis_copy[next_cave] += 1
                paths += traverse_caves_dfs(small_cave_max_visit, next_cave, vis_copy, end_cave)
        else:
            paths += traverse_caves_dfs(small_cave_max_visit, next_cave, visited, end_cave)
    return paths

@timed("Part01:")
def part01():
    return traverse_caves_dfs(1)

@timed("Part02:")
def part02():
    return traverse_caves_dfs(2)

if __name__ == '__main__':
    part01()
    part02()
