package sorting

import (
	"fmt"
	"slices"
	"strings"
)

type Sort struct {
	By    string
	Order string
}

func NewSort(by string, order string) Sort {
	return Sort{
		By:    by,
		Order: order,
	}
}
func (s *Sort) Validate(allowed []string) error {
	if s.By == "" {
		return nil
	}
	if !slices.Contains(allowed, s.By) {
		return fmt.Errorf("sort by %s is not allowed", s.By)
	}
	s.Order = strings.ToLower(s.Order)
	if s.Order != "asc" && s.Order != "desc" {
		s.Order = ""
	}
	return nil
}
