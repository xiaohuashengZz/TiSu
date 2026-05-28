import request from '../utils/request'

export function getTypes(group) {
  return request.get(`/api/types/${group}`)
}
