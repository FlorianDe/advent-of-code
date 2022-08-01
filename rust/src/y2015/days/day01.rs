use crate::aoc::Solution;

fn bracket_to_direction(c: char) -> i32 {
    return match c {
        '(' => 1,
        ')' => -1,
        _ => 0
    };
}

pub struct Day01 {}

impl Solution for Day01 {
    fn part01(&self, input: String) -> Option<String> {
        let sum: i32 = input.chars().map(bracket_to_direction).sum();
        return Some(sum.to_string());
    }

    fn part02(&self, input: String) -> Option<String> {
        let mut count = 0;
        for (idx, c) in input.chars().enumerate() {
            count += bracket_to_direction(c);
            if count == -1 {
                return Some(((idx + 1) as i32).to_string());
            }
        }
        return None;
    }
}