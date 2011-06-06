/*************************************************************************
 *
 * RST GROUP CONFIDENTIAL
 * ______________________
 *
 * [2007] - [2011] RST Group Inc.
 * All Rights Reserved.
 *
 * NOTICE: All information contained herein is, and remains the property
 * of RST Group Inc. and its suppliers, if any. The intellectual and
 * technical concepts contained herein are proprietary to RST Group Inc.
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret and copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from RST Group Inc.
 */
package org.mongeez.commands;

import org.mongeez.dao.MongeezDao;

/**
 * @author oleksii
 * @since 5/3/11
 */
public class Script {
    private String body;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void run(MongeezDao dao) {
        dao.runScript(body);
    }
}
