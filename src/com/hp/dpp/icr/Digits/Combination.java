package com.hp.dpp.icr.Digits;

import java.util.Set;

/**
 * Date: Nov 22, 2003
 * Time: 6:22:33 PM
 */
   class Combination {
        int first_;
        int second_;
        Set set_;

        Combination(int i, int j, Set set) {
            first_ = i;
            second_ = j;
            set_ = set;
        }

        int getFirstValue() {
            return first_;
        }

        int getSecondValue() {
            return second_;
        }

        boolean containsValue(int value) {
            return (value == first_ || value == second_);
        }

        Set getSet() {
            return set_;
        }

        int getOtherValue(int value) {
            int toRet;
            if (value == first_) {
                toRet = second_;
            } else {
                toRet = first_;
            }
            return toRet;
        }
    }
