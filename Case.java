package AlgoSolver;

import java.util.BitSet;

public class Case {
    private BitSet m_bitset;
    
    Case() {
        m_bitset = new BitSet(MapEnum.values().length);
    }
    
    boolean contains(MapEnum enumeration) {
        return m_bitset.get(MapEnum.getValue(enumeration));
    }
    
    void reset(MapEnum enumeration) {
        m_bitset.clear(MapEnum.getValue(enumeration));
    }
    
    void set(MapEnum enumeration) {
        m_bitset.set(MapEnum.getValue(enumeration));
    }
}