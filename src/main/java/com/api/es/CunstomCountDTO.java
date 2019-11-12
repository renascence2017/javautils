package com.api.es;

import lombok.Data;

import java.util.List;

@Data
public class CunstomCountDTO {


    /**
     * aggregations : {"custom_id":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"doc_count":100,"key":"10000"}]}}
     * hitsData : []
     * success : true
     * totalHits : 100
     */

    private AggregationsBean aggregations;
    private boolean success;
    private int totalHits;
    private List<?> hitsData;

    @Data
    public static class AggregationsBean {
        /**
         * custom_id : {"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"doc_count":100,"key":"10000"}]}
         */

        private CustomIdBean custom_id;


        @Data
        public static class CustomIdBean {
            /**
             * doc_count_error_upper_bound : 0
             * sum_other_doc_count : 0
             * buckets : [{"doc_count":100,"key":"10000"}]
             */

            private List<BucketsBean> buckets;


            @Data
            public static class BucketsBean {
                /**
                 * doc_count : 100
                 * key : 10000
                 */

                private Long doc_count;
                private String key;

            }
        }
    }
}
