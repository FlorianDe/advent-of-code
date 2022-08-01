use std::collections::HashSet;
use crate::aoc::Solution;
use crate::aoc::Point;

pub struct Day01 {}

fn get_hq_distance(pos: Point<i64>) -> String {
    (pos.x.abs()+pos.y.abs()).to_string()
}

fn navigate(input: String, part_b: bool) -> Option<String> {
    let mut visited_places = HashSet::new();
    let directions = [Point { x: 0, y: 1 }, Point { x: 1, y: 0 }, Point { x: 0, y: -1 }, Point { x: -1, y: 0 }];
    let directions_len: i32 = directions.len() as i32;
    let mut current_direction: i32 = 0;
    let mut cur_pos = Point { x: 0, y: 0 };
    for op in input.split(", ") {
        let (rotation, distance) = op.split_at(1);
        current_direction = (current_direction + match rotation {
            "R" => 1,
            _ => -1
        }).rem_euclid(directions_len);
        for _d in 0..distance.parse::<i64>().unwrap(){
            cur_pos = cur_pos + directions[current_direction as usize];
            if visited_places.contains(&cur_pos) && part_b {
                return Some(get_hq_distance(cur_pos));
            }
            visited_places.insert(cur_pos);
        }
    }
    return Some(get_hq_distance(cur_pos));
}

impl Solution for Day01 {
    fn part01(&self, input: String) -> Option<String> {
        return navigate(input, false);
    }

    fn part02(&self, input: String) -> Option<String> {
        return navigate(input, true);
    }
}