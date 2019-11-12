package com.api.es;

import lombok.Data;

import java.util.List;

@Data
public class EsTaskIdResultDTO {
    /**
     * aggregations : {"task_id":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"doc_count":3,"template_id":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"doc_count":3,"carrier":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"receipt_status":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"custom_bill_sum":{"value":0},"doc_count":3,"supplier_bill_sum":{"value":0},"sms_count":{"value":0},"key":"123456"}]},"doc_count":3,"key":"123456"}]},"key":"0"}]},"key":"0"}]}}
     * hitsData : []
     * success : true
     * totalHits : 4
     */

    private AggregationsBean aggregations;
    private boolean success;
    private int totalHits;
//    private List<?> hitsData;

    @Data
    public static class AggregationsBean {
        /**
         * task_id : {"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"doc_count":3,"template_id":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"doc_count":3,"carrier":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"receipt_status":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"custom_bill_sum":{"value":0},"doc_count":3,"supplier_bill_sum":{"value":0},"sms_count":{"value":0},"key":"123456"}]},"doc_count":3,"key":"123456"}]},"key":"0"}]},"key":"0"}]}
         */

        private TaskIdBean task_id;

        @Data
        public static class TaskIdBean {
            /**
             * doc_count_error_upper_bound : 0
             * sum_other_doc_count : 0
             * buckets : [{"doc_count":3,"template_id":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"doc_count":3,"carrier":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"receipt_status":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"custom_bill_sum":{"value":0},"doc_count":3,"supplier_bill_sum":{"value":0},"sms_count":{"value":0},"key":"123456"}]},"doc_count":3,"key":"123456"}]},"key":"0"}]},"key":"0"}]
             */

            private int doc_count_error_upper_bound;
            private int sum_other_doc_count;
            private List<BucketsBeanXXX> buckets;

            @Data
            public static class BucketsBeanXXX {
                /**
                 * doc_count : 3
                 * template_id : {"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"doc_count":3,"carrier":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"receipt_status":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"custom_bill_sum":{"value":0},"doc_count":3,"supplier_bill_sum":{"value":0},"sms_count":{"value":0},"key":"123456"}]},"doc_count":3,"key":"123456"}]},"key":"0"}]}
                 * key : 0
                 */

                private int doc_count;
                private TemplateIdBean template_id;
                private String key;


                @Data
                public static class TemplateIdBean {
                    /**
                     * doc_count_error_upper_bound : 0
                     * sum_other_doc_count : 0
                     * buckets : [{"doc_count":3,"carrier":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"receipt_status":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"custom_bill_sum":{"value":0},"doc_count":3,"supplier_bill_sum":{"value":0},"sms_count":{"value":0},"key":"123456"}]},"doc_count":3,"key":"123456"}]},"key":"0"}]
                     */

                    private int doc_count_error_upper_bound;
                    private int sum_other_doc_count;
                    private List<BucketsBeanXX> buckets;


                    @Data
                    public static class BucketsBeanXX {
                        /**
                         * doc_count : 3
                         * carrier : {"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"receipt_status":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"custom_bill_sum":{"value":0},"doc_count":3,"supplier_bill_sum":{"value":0},"sms_count":{"value":0},"key":"123456"}]},"doc_count":3,"key":"123456"}]}
                         * key : 0
                         */

                        private int doc_count;
                        private CarrierBean carrier;
                        private String key;

                        @Data
                        public static class CarrierBean {
                            /**
                             * doc_count_error_upper_bound : 0
                             * sum_other_doc_count : 0
                             * buckets : [{"receipt_status":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"custom_bill_sum":{"value":0},"doc_count":3,"supplier_bill_sum":{"value":0},"sms_count":{"value":0},"key":"123456"}]},"doc_count":3,"key":"123456"}]
                             */

                            private int doc_count_error_upper_bound;
                            private int sum_other_doc_count;
                            private List<BucketsBeanX> buckets;


                            @Data
                            public static class BucketsBeanX {
                                /**
                                 * receipt_status : {"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"custom_bill_sum":{"value":0},"doc_count":3,"supplier_bill_sum":{"value":0},"sms_count":{"value":0},"key":"123456"}]}
                                 * doc_count : 3
                                 * key : 123456
                                 */

                                private ReceiptStatusBean receipt_status;
                                private int doc_count;
                                private String key;

                                @Data
                                public static class ReceiptStatusBean {
                                    /**
                                     * doc_count_error_upper_bound : 0
                                     * sum_other_doc_count : 0
                                     * buckets : [{"custom_bill_sum":{"value":0},"doc_count":3,"supplier_bill_sum":{"value":0},"sms_count":{"value":0},"key":"123456"}]
                                     */

                                    private int doc_count_error_upper_bound;
                                    private int sum_other_doc_count;
                                    private List<BucketsBean> buckets;


                                    @Data
                                    public static class BucketsBean {
                                        /**
                                         * custom_bill_sum : {"value":0}
                                         * doc_count : 3
                                         * supplier_bill_sum : {"value":0}
                                         * sms_count : {"value":0}
                                         * key : 123456
                                         */

                                        private CustomBillSumBean custom_bill_sum;
                                        private int doc_count;
                                        private SupplierBillSumBean supplier_bill_sum;
                                        private SmsCountBean sms_count;
                                        private String key;

                                        @Data
                                        public static class CustomBillSumBean {
                                            /**
                                             * value : 0.0
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

                                        @Data
                                        public static class SmsCountBean {
                                            /**
                                             * value : 0
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
    }
}
