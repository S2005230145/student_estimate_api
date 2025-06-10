package actor;

import utils.BalanceParam;

/**
 * Actor对象
 */
public class ActorProtocol {


    public static class CACHE_POS_PRODUCTS {
        public CACHE_POS_PRODUCTS() {
        }
    }

    public static class PRINT_FEIER {
        public String param;
        public String orderNo;
        public int printerType;

        public PRINT_FEIER(String param, String orderNo, int printerType) {
            this.param = param;
            this.orderNo = orderNo;
            this.printerType = printerType;
        }
    }

    public static class BALANCE_LOG {
        public long uid;
        public int itemId;
        public BalanceParam balanceParam;

        public BALANCE_LOG(long uid, int itemId, BalanceParam balanceParam) {
            this.uid = uid;
            this.itemId = itemId;
            this.balanceParam = balanceParam;
        }
    }

    public static class CACHE_PAY_RESULT {
        public CACHE_PAY_RESULT() {
        }
    }

    public static class HANDLE_PAY_RESULT {
        public String orderNo;
        public String txNo;
        public String payDetail;

        public HANDLE_PAY_RESULT(String orderNo, String txNo, String payDetail) {
            this.orderNo = orderNo;
            this.txNo = txNo;
            this.payDetail = payDetail;
        }
    }

    public static class REPORT_DAY {
        public REPORT_DAY() {
        }
    }

    public static class REPORT_LAST_DAY {
        public REPORT_LAST_DAY() {
        }
    }


    public static class REFUND_LIST {
        public REFUND_LIST() {
        }
    }

    public static class REFUND_EACH {
        public long refundId;

        public REFUND_EACH(long refundId) {
            this.refundId = refundId;
        }
    }
}
