export function dist(p1, p2) {
  return Math.sqrt(Math.pow(p2[0] - p1[0], 2) + Math.pow(p2[1] - p1[1], 2));
}

export function angle_of_direction(p1, p2) {
  return Math.atan2(p2[1] - p1[1], p2[0] - p1[0]);
}

export function point_at_distance_and_angle(p1, dist, rad) {
  return [p1[0] + dist * Math.cos(rad), p1[1] + dist * Math.sin(rad)];
}

export function point_at_distance_towards_direction(p1, dist, pdir) {
  return point_at_distance_and_angle(p1, dist, angle_of_direction(p1, pdir));
}
