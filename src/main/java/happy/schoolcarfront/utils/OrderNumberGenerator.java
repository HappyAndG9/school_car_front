package happy.schoolcarfront.utils;


/**
 * @Author 木月丶
 * @Description 通过雪花算法生成订单号
 */
public class OrderNumberGenerator {
    private static final long START_TIMESTAMP = System.currentTimeMillis(); // 起始的时间戳，可根据实际需求修改
    private static final long SEQUENCE_BIT = 12; // 序列号占用的位数
    private static final long MACHINE_BIT = 5; // 机器标识占用的位数
    private static final long DATACENTER_BIT = 5; // 数据中心占用的位数

    private static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT); // 序列号最大值
    private static final long MAX_DATACENTER = ~(-1L << DATACENTER_BIT); // 数据中心最大值
    private static final long MAX_MACHINE = ~(-1L << MACHINE_BIT); // 机器标识最大值

    private static final long MACHINE_LEFT_SHIFT = SEQUENCE_BIT; // 机器标识左移位数
    private static final long DATACENTER_LEFT_SHIFT = SEQUENCE_BIT + MACHINE_BIT; // 数据中心左移位数
    private static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BIT + MACHINE_BIT + DATACENTER_BIT; // 时间戳左移位数

    private final long datacenterId; // 数据中心ID
    private final long machineId; // 机器标识ID
    private long sequence = 0L; // 序列号
    private long lastTimestamp = -1L; // 上一次生成ID的时间戳

    public OrderNumberGenerator(long datacenterId, long machineId) {
        if (datacenterId > MAX_DATACENTER || datacenterId < 0) {
            throw new IllegalArgumentException("数据中心ID超出范围");
        }
        if (machineId > MAX_MACHINE || machineId < 0) {
            throw new IllegalArgumentException("机器标识ID超出范围");
        }
        this.datacenterId = datacenterId;
        this.machineId = machineId;
    }

    public synchronized long nextId() {
        long currentTimestamp = System.currentTimeMillis();
        if (currentTimestamp < lastTimestamp) {
            throw new RuntimeException("时间戳发生回退");
        }
        if (currentTimestamp == lastTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0) {
                currentTimestamp = nextTimestamp(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }
        lastTimestamp = currentTimestamp;
        return ((currentTimestamp - START_TIMESTAMP) << TIMESTAMP_LEFT_SHIFT)
                | (datacenterId << DATACENTER_LEFT_SHIFT)
                | (machineId << MACHINE_LEFT_SHIFT)
                | sequence;
    }

    private long nextTimestamp(long lastTimestamp) {
        long currentTimestamp = System.currentTimeMillis();
        while (currentTimestamp <= lastTimestamp) {
            currentTimestamp = System.currentTimeMillis();
        }
        return currentTimestamp;
    }
}
