
package pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Data
public class Block {

    private String hash;
    private Integer confirmations;
    private Integer strippedsize;
    private Integer size;
    private Integer weight;
    private Integer height;
    private Integer version;
    private String versionHex;
    private String merkleroot;
    private List<String> tx = new ArrayList<String>();
    private Integer time;
    private Integer mediantime;
    private Long nonce;
    private String bits;
    private Double difficulty;
    private String chainwork;
    private Integer nTx;
    private String previousblockhash;
    private String nextblockhash;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(hash).append(confirmations).append(strippedsize).append(size).append(weight).append(height).append(version).append(versionHex).append(merkleroot).append(tx).append(time).append(mediantime).append(nonce).append(bits).append(difficulty).append(chainwork).append(nTx).append(previousblockhash).append(nextblockhash).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Block) == false) {
            return false;
        }
        Block rhs = ((Block) other);
        return new EqualsBuilder().append(hash, rhs.hash).append(confirmations, rhs.confirmations).append(strippedsize, rhs.strippedsize).append(size, rhs.size).append(weight, rhs.weight).append(height, rhs.height).append(version, rhs.version).append(versionHex, rhs.versionHex).append(merkleroot, rhs.merkleroot).append(tx, rhs.tx).append(time, rhs.time).append(mediantime, rhs.mediantime).append(nonce, rhs.nonce).append(bits, rhs.bits).append(difficulty, rhs.difficulty).append(chainwork, rhs.chainwork).append(nTx, rhs.nTx).append(previousblockhash, rhs.previousblockhash).append(nextblockhash, rhs.nextblockhash).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
