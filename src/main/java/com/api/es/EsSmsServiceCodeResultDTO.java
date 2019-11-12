package com.api.es;

import lombok.Data;

import java.util.List;

@Data
public class EsSmsServiceCodeResultDTO {


    /**
     * aggregations : {"sms_service_code":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"doc_count":100,"carrier":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"receipt_status":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"custom_bill_sum":{"value":182},"doc_count":91,"supplier_bill_sum":{"value":0},"key":"FAIL"}]},"doc_count":91,"key":"CUCC"},{"receipt_status":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"custom_bill_sum":{"value":16},"doc_count":9,"supplier_bill_sum":{"value":0},"key":"FAIL"}]},"doc_count":9,"key":"CMCC"}]},"key":""},{"doc_count":30,"carrier":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"receipt_status":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"custom_bill_sum":{"value":52},"doc_count":26,"supplier_bill_sum":{"value":0},"key":"FAIL"}]},"doc_count":26,"key":"CUCC"},{"receipt_status":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"custom_bill_sum":{"value":4},"doc_count":2,"supplier_bill_sum":{"value":0},"key":"UNKNOWN"}]},"doc_count":2,"key":"CMCC"},{"receipt_status":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"custom_bill_sum":{"value":4},"doc_count":2,"supplier_bill_sum":{"value":0},"key":"FAIL"}]},"doc_count":2,"key":"CTCC"}]},"key":"bst11"},{"doc_count":6,"carrier":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"receipt_status":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"custom_bill_sum":{"value":5},"doc_count":5,"supplier_bill_sum":{"value":36},"key":"FAIL"},{"custom_bill_sum":{"value":1},"doc_count":1,"supplier_bill_sum":{"value":9},"key":"UNKNOWN"}]},"doc_count":6,"key":"CUCC"}]},"key":"22"}]}}
     * hitsData : []
     * success : true
     * totalHits : 178
     */

    private AggregationsBean aggregations;
    private boolean success;
    private int totalHits;

    @Data
    public static class AggregationsBean {
        /**
         * sms_service_code : {"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"doc_count":100,"carrier":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"receipt_status":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"custom_bill_sum":{"value":182},"doc_count":91,"supplier_bill_sum":{"value":0},"key":"FAIL"}]},"doc_count":91,"key":"CUCC"},{"receipt_status":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"custom_bill_sum":{"value":16},"doc_count":9,"supplier_bill_sum":{"value":0},"key":"FAIL"}]},"doc_count":9,"key":"CMCC"}]},"key":""},{"doc_count":30,"carrier":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"receipt_status":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"custom_bill_sum":{"value":52},"doc_count":26,"supplier_bill_sum":{"value":0},"key":"FAIL"}]},"doc_count":26,"key":"CUCC"},{"receipt_status":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"custom_bill_sum":{"value":4},"doc_count":2,"supplier_bill_sum":{"value":0},"key":"UNKNOWN"}]},"doc_count":2,"key":"CMCC"},{"receipt_status":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"custom_bill_sum":{"value":4},"doc_count":2,"supplier_bill_sum":{"value":0},"key":"FAIL"}]},"doc_count":2,"key":"CTCC"}]},"key":"bst11"},{"doc_count":6,"carrier":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"receipt_status":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"custom_bill_sum":{"value":5},"doc_count":5,"supplier_bill_sum":{"value":36},"key":"FAIL"},{"custom_bill_sum":{"value":1},"doc_count":1,"supplier_bill_sum":{"value":9},"key":"UNKNOWN"}]},"doc_count":6,"key":"CUCC"}]},"key":"22"}]}
         */

        private SmsServiceCodeBean sms_service_code;


        @Data
        public static class SmsServiceCodeBean {
            /**
             * doc_count_error_upper_bound : 0
             * sum_other_doc_count : 0
             * buckets : [{"doc_count":100,"carrier":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"receipt_status":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"custom_bill_sum":{"value":182},"doc_count":91,"supplier_bill_sum":{"value":0},"key":"FAIL"}]},"doc_count":91,"key":"CUCC"},{"receipt_status":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"custom_bill_sum":{"value":16},"doc_count":9,"supplier_bill_sum":{"value":0},"key":"FAIL"}]},"doc_count":9,"key":"CMCC"}]},"key":""},{"doc_count":30,"carrier":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"receipt_status":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"custom_bill_sum":{"value":52},"doc_count":26,"supplier_bill_sum":{"value":0},"key":"FAIL"}]},"doc_count":26,"key":"CUCC"},{"receipt_status":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"custom_bill_sum":{"value":4},"doc_count":2,"supplier_bill_sum":{"value":0},"key":"UNKNOWN"}]},"doc_count":2,"key":"CMCC"},{"receipt_status":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"custom_bill_sum":{"value":4},"doc_count":2,"supplier_bill_sum":{"value":0},"key":"FAIL"}]},"doc_count":2,"key":"CTCC"}]},"key":"bst11"},{"doc_count":6,"carrier":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"receipt_status":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"custom_bill_sum":{"value":5},"doc_count":5,"supplier_bill_sum":{"value":36},"key":"FAIL"},{"custom_bill_sum":{"value":1},"doc_count":1,"supplier_bill_sum":{"value":9},"key":"UNKNOWN"}]},"doc_count":6,"key":"CUCC"}]},"key":"22"}]
             */

            private int doc_count_error_upper_bound;
            private int sum_other_doc_count;
            private List<BucketsBeanXX> buckets;


            @Data
            public static class BucketsBeanXX {
                /**
                 * doc_count : 100
                 * carrier : {"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"receipt_status":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"custom_bill_sum":{"value":182},"doc_count":91,"supplier_bill_sum":{"value":0},"key":"FAIL"}]},"doc_count":91,"key":"CUCC"},{"receipt_status":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"custom_bill_sum":{"value":16},"doc_count":9,"supplier_bill_sum":{"value":0},"key":"FAIL"}]},"doc_count":9,"key":"CMCC"}]}
                 * key :
                 */

                private int doc_count;
                private CarrierBean carrier;
                private String key;


                @Data
                public static class CarrierBean {
                    /**
                     * doc_count_error_upper_bound : 0
                     * sum_other_doc_count : 0
                     * buckets : [{"receipt_status":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"custom_bill_sum":{"value":182},"doc_count":91,"supplier_bill_sum":{"value":0},"key":"FAIL"}]},"doc_count":91,"key":"CUCC"},{"receipt_status":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"custom_bill_sum":{"value":16},"doc_count":9,"supplier_bill_sum":{"value":0},"key":"FAIL"}]},"doc_count":9,"key":"CMCC"}]
                     */

                    private int doc_count_error_upper_bound;
                    private int sum_other_doc_count;
                    private List<BucketsBeanX> buckets;


                    @Data
                    public static class BucketsBeanX {
                        /**
                         * receipt_status : {"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"custom_bill_sum":{"value":182},"doc_count":91,"supplier_bill_sum":{"value":0},"key":"FAIL"}]}
                         * doc_count : 91
                         * key : CUCC
                         */

                        private ReceiptStatusBean receipt_status;
                        private int doc_count;
                        private String key;


                        @Data
                        public static class ReceiptStatusBean {
                            /**
                             * doc_count_error_upper_bound : 0
                             * sum_other_doc_count : 0
                             * buckets : [{"custom_bill_sum":{"value":182},"doc_count":91,"supplier_bill_sum":{"value":0},"key":"FAIL"}]
                             */

                            private int doc_count_error_upper_bound;
                            private int sum_other_doc_count;
                            private List<BucketsBean> buckets;


                            @Data
                            public static class BucketsBean {
                                /**
                                 * custom_bill_sum : {"value":182}
                                 * doc_count : 91
                                 * supplier_bill_sum : {"value":0}
                                 * key : FAIL
                                 */

                                private CustomBillSumBean custom_bill_sum;
                                private int doc_count;
                                private SupplierBillSumBean supplier_bill_sum;
                                private String key;


                                @Data
                                public static class CustomBillSumBean {
                                    /**
                                     * value : 182.0
                                     */

                                    private int value;


                                }

                                @Data
                                public static class SupplierBillSumBean {
                                    /**
                                     * value : 0.0
                                     */

                                    private int value;


                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
