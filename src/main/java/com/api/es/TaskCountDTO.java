package com.api.es;

import lombok.Data;

import java.util.List;

@Data
public class TaskCountDTO {


    /**
     * aggregations : {"receipt_status":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"doc_count":13,"key":"UNKNOWN"}]}}
     * hitsData : []
     * success : true
     * totalHits : 13
     */

    private AggregationsBean aggregations;
    private boolean success;
    private Long totalHits;

    @Data
    public static class AggregationsBean {
        /**
         * receipt_status : {"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"doc_count":13,"key":"UNKNOWN"}]}
         */

        private ReceiptStatusBean receipt_status;


        @Data
        public static class ReceiptStatusBean {
            /**
             * doc_count_error_upper_bound : 0
             * sum_other_doc_count : 0
             * buckets : [{"doc_count":13,"key":"UNKNOWN"}]
             */

            private List<BucketsBean> buckets;


            @Data
            public static class BucketsBean {
                /**
                 * doc_count : 13
                 * key : UNKNOWN
                 */

                private Long doc_count;
                private String key;


            }
        }
    }
}
