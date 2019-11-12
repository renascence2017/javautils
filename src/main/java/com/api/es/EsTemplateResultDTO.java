package com.api.es;

import java.util.List;

public class EsTemplateResultDTO {


    /**
     * aggregations : {"template_id":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"doc_count":2,"carrier":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"receipt_status":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"custom_bill_sum":{"value":2},"doc_count":2,"supplier_bill_sum":{"value":0},"key":"FAIL"}]},"doc_count":2,"key":"CUCC"}]},"key":"62"}]}}
     * hitsData : []
     * success : true
     * totalHits : 2
     */

    private AggregationsBean aggregations;
    private boolean success;
    private List<?> hitsData;

    public AggregationsBean getAggregations() {
        return aggregations;
    }

    public void setAggregations(AggregationsBean aggregations) {
        this.aggregations = aggregations;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<?> getHitsData() {
        return hitsData;
    }

    public void setHitsData(List<?> hitsData) {
        this.hitsData = hitsData;
    }

    public static class AggregationsBean {
        /**
         * template_id : {"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"doc_count":2,"carrier":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"receipt_status":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"custom_bill_sum":{"value":2},"doc_count":2,"supplier_bill_sum":{"value":0},"key":"FAIL"}]},"doc_count":2,"key":"CUCC"}]},"key":"62"}]}
         */

        private TemplateIdBean template_id;

        public TemplateIdBean getTemplate_id() {
            return template_id;
        }

        public void setTemplate_id(TemplateIdBean template_id) {
            this.template_id = template_id;
        }

        public static class TemplateIdBean {
            /**
             * doc_count_error_upper_bound : 0
             * sum_other_doc_count : 0
             * buckets : [{"doc_count":2,"carrier":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"receipt_status":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"custom_bill_sum":{"value":2},"doc_count":2,"supplier_bill_sum":{"value":0},"key":"FAIL"}]},"doc_count":2,"key":"CUCC"}]},"key":"62"}]
             */

            private int doc_count_error_upper_bound;
            private int sum_other_doc_count;
            private List<BucketsBeanXX> buckets;

            public int getDoc_count_error_upper_bound() {
                return doc_count_error_upper_bound;
            }

            public void setDoc_count_error_upper_bound(int doc_count_error_upper_bound) {
                this.doc_count_error_upper_bound = doc_count_error_upper_bound;
            }

            public int getSum_other_doc_count() {
                return sum_other_doc_count;
            }

            public void setSum_other_doc_count(int sum_other_doc_count) {
                this.sum_other_doc_count = sum_other_doc_count;
            }

            public List<BucketsBeanXX> getBuckets() {
                return buckets;
            }

            public void setBuckets(List<BucketsBeanXX> buckets) {
                this.buckets = buckets;
            }

            public static class BucketsBeanXX {
                /**
                 * doc_count : 2
                 * carrier : {"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"receipt_status":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"custom_bill_sum":{"value":2},"doc_count":2,"supplier_bill_sum":{"value":0},"key":"FAIL"}]},"doc_count":2,"key":"CUCC"}]}
                 * key : 62
                 */

                private int doc_count;
                private CarrierBean carrier;
                private String key;

                public int getDoc_count() {
                    return doc_count;
                }

                public void setDoc_count(int doc_count) {
                    this.doc_count = doc_count;
                }

                public CarrierBean getCarrier() {
                    return carrier;
                }

                public void setCarrier(CarrierBean carrier) {
                    this.carrier = carrier;
                }

                public String getKey() {
                    return key;
                }

                public void setKey(String key) {
                    this.key = key;
                }

                public static class CarrierBean {
                    /**
                     * doc_count_error_upper_bound : 0
                     * sum_other_doc_count : 0
                     * buckets : [{"receipt_status":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"custom_bill_sum":{"value":2},"doc_count":2,"supplier_bill_sum":{"value":0},"key":"FAIL"}]},"doc_count":2,"key":"CUCC"}]
                     */

                    private int doc_count_error_upper_bound;
                    private int sum_other_doc_count;
                    private List<BucketsBeanX> buckets;

                    public int getDoc_count_error_upper_bound() {
                        return doc_count_error_upper_bound;
                    }

                    public void setDoc_count_error_upper_bound(int doc_count_error_upper_bound) {
                        this.doc_count_error_upper_bound = doc_count_error_upper_bound;
                    }

                    public int getSum_other_doc_count() {
                        return sum_other_doc_count;
                    }

                    public void setSum_other_doc_count(int sum_other_doc_count) {
                        this.sum_other_doc_count = sum_other_doc_count;
                    }

                    public List<BucketsBeanX> getBuckets() {
                        return buckets;
                    }

                    public void setBuckets(List<BucketsBeanX> buckets) {
                        this.buckets = buckets;
                    }

                    public static class BucketsBeanX {
                        /**
                         * receipt_status : {"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"custom_bill_sum":{"value":2},"doc_count":2,"supplier_bill_sum":{"value":0},"key":"FAIL"}]}
                         * doc_count : 2
                         * key : CUCC
                         */

                        private ReceiptStatusBean receipt_status;
                        private int doc_count;
                        private String key;

                        public ReceiptStatusBean getReceipt_status() {
                            return receipt_status;
                        }

                        public void setReceipt_status(ReceiptStatusBean receipt_status) {
                            this.receipt_status = receipt_status;
                        }

                        public int getDoc_count() {
                            return doc_count;
                        }

                        public void setDoc_count(int doc_count) {
                            this.doc_count = doc_count;
                        }

                        public String getKey() {
                            return key;
                        }

                        public void setKey(String key) {
                            this.key = key;
                        }

                        public static class ReceiptStatusBean {
                            /**
                             * doc_count_error_upper_bound : 0
                             * sum_other_doc_count : 0
                             * buckets : [{"custom_bill_sum":{"value":2},"doc_count":2,"supplier_bill_sum":{"value":0},"key":"FAIL"}]
                             */

                            private int doc_count_error_upper_bound;
                            private int sum_other_doc_count;
                            private List<BucketsBean> buckets;

                            public int getDoc_count_error_upper_bound() {
                                return doc_count_error_upper_bound;
                            }

                            public void setDoc_count_error_upper_bound(int doc_count_error_upper_bound) {
                                this.doc_count_error_upper_bound = doc_count_error_upper_bound;
                            }

                            public int getSum_other_doc_count() {
                                return sum_other_doc_count;
                            }

                            public void setSum_other_doc_count(int sum_other_doc_count) {
                                this.sum_other_doc_count = sum_other_doc_count;
                            }

                            public List<BucketsBean> getBuckets() {
                                return buckets;
                            }

                            public void setBuckets(List<BucketsBean> buckets) {
                                this.buckets = buckets;
                            }

                            public static class BucketsBean {
                                /**
                                 * custom_bill_sum : {"value":2}
                                 * doc_count : 2
                                 * supplier_bill_sum : {"value":0}
                                 * key : FAIL
                                 */

                                private CustomBillSumBean custom_bill_sum;
                                private int doc_count;
                                private SupplierBillSumBean supplier_bill_sum;
                                private String key;

                                public CustomBillSumBean getCustom_bill_sum() {
                                    return custom_bill_sum;
                                }

                                public void setCustom_bill_sum(CustomBillSumBean custom_bill_sum) {
                                    this.custom_bill_sum = custom_bill_sum;
                                }

                                public int getDoc_count() {
                                    return doc_count;
                                }

                                public void setDoc_count(int doc_count) {
                                    this.doc_count = doc_count;
                                }

                                public SupplierBillSumBean getSupplier_bill_sum() {
                                    return supplier_bill_sum;
                                }

                                public void setSupplier_bill_sum(SupplierBillSumBean supplier_bill_sum) {
                                    this.supplier_bill_sum = supplier_bill_sum;
                                }

                                public String getKey() {
                                    return key;
                                }

                                public void setKey(String key) {
                                    this.key = key;
                                }

                                public static class CustomBillSumBean {
                                    /**
                                     * value : 2.0
                                     */

                                    private Integer value;

                                    public Integer getValue() {
                                        return value;
                                    }

                                    public void setValue(Integer value) {
                                        this.value = value;
                                    }
                                }

                                public static class SupplierBillSumBean {
                                    /**
                                     * value : 0.0
                                     */

                                    private Integer value;

                                    public Integer getValue() {
                                        return value;
                                    }

                                    public void setValue(Integer value) {
                                        this.value = value;
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
